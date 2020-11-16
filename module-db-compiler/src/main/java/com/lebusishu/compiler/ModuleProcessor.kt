package com.lebusishu.compiler

import com.google.auto.common.SuperficialValidation
import com.google.auto.service.AutoService
import com.lebusishu.TypeConfig
import com.lebusishu.annotations.ModuleDBConfig
import com.lebusishu.annotations.ModuleDBVariable
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.io.IOException
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashSet

/**
 * Description : Process the kotlin file with {@link ModuleDBConfig} and {@link ModuleDBVariable} annotation
 * Create by wxh on 2020/10/13
 * Phone ：15233620521
 * Email：wangxiaohui1118@gmail.com
 * Person in charge : lebusishu
 */
@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class ModuleProcessor : AbstractProcessor() {
    private var processCount: Int = 0

    /**
     * 生成文件的工具类
     */
    private lateinit var filer: Filer
    override fun getSupportedAnnotationTypes(): Set<String> {
        val types: LinkedHashSet<String> = LinkedHashSet()
        for (annotation in getSupportedAnnotations()) {
            types.add(annotation.canonicalName)
        }
        return types
    }

    /**
     * 设置支持的注解类型
     *
     */
    private fun getSupportedAnnotations(): Set<Class<out Annotation>> {
        return setOf(ModuleDBConfig::class.java)
    }

    /**
     * 设置支持的版本
     *
     * @return 这里用最新的就好
     */
    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        filer = processingEnv?.filer!!
    }

    /**
     * 注解内部逻辑的实现
     * <p>
     * Element代表程序的一个元素，可以是package, class, interface, method.只在编译期存在
     * TypeElement：变量；TypeElement：类或者接口
     */
    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
        if (processCount > 0) {
            return false
        }
        //构造函数,保存所有数据库创建信息
        val constructor = FunSpec.constructorBuilder()
        constructor
            .addStatement("this.tablesMapping = %T<String,Any>()", HashMap::class)
            .addStatement("this.versionsMapping = %T<String,Int>()", HashMap::class)
            .addStatement("this.pathMapping = %T<String,String>()", HashMap::class)
            .addStatement("this.updateTableMapping = %T<String,String>()", HashMap::class)
            .addStatement("this.deleteTableMapping = %T<String,String>()", HashMap::class)
        val file: FileSpec = roundEnv?.let { findAndParseTargets(it, constructor) } ?: return false
        try {
            file.writeTo(filer)
            processCount++
        } catch (e: IOException) {
            e.message?.let {
                error(
                    "Unable to write same name ${file.packageName}: $it"
                )
            }
        }

        return false
    }

    private fun error(msg: String, vararg args: Any) {
        recordMessage(msg, args)
    }

    private fun recordMessage(msg: String, vararg args: Any) {
        var message = msg
        if (args.isNotEmpty()) {
            message = String.format(msg, args)
        }
        processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, message)
    }

    /**
     * find all module db config
     */
    private fun findAndParseTargets(
        env: RoundEnvironment,
        constructor: FunSpec.Builder
    ): FileSpec? {
        var file: FunSpec.Builder = constructor
        // 1、获取要处理的注解的元素的集合
        env.getElementsAnnotatedWith(ModuleDBConfig::class.java).forEachIndexed { index, element ->
            if (!SuperficialValidation.validateElement(element)) {
                return@forEachIndexed
            }
            val annotation: ModuleDBConfig = element.getAnnotation(ModuleDBConfig::class.java)
            val dbName: String = annotation.dbName
            val dbVersion: Int = annotation.dbVersion
            if (dbName.isEmpty()) {
                return@forEachIndexed
            }
            //返回在此类或接口中直接声明的字段，方法，构造函数和成员类型
            val allFile: List<Element> = element.enclosedElements
            file.addStatement("val tables$index = %T<String>()", ArrayList::class)
            file = parseModuleDB(dbName,dbVersion, allFile, file, index)
        }
        val nameFile = PREFIX + "sqls"
        //指定生成的文件名
        return FileSpec.builder(PACKAGE_NAME, nameFile)
            .addType(
                TypeSpec.classBuilder(nameFile)
                    .primaryConstructor(file.build())
                    //添加属性
                    .addProperties(buildRouterModuleFields())
                    .addKdoc(FILE_DOC)
                    .build()
            )
            .build()
    }

    /**
     * parse router module class
     */
    private fun parseModuleDB(
        dbName: String,
        dbVersion: Int,
        allFile: List<Element>,
        constructor: FunSpec.Builder,
        index: Int
    ): FunSpec.Builder {
        var dbPath = ""
        var updates = ""
        var deletes = ""
        // parse ModuleDBVariable：解析注解的方法并添加到集合里
        for (element in allFile) {
            val path: ModuleDBVariable =
                element.getAnnotation(ModuleDBVariable::class.java) ?: continue
            val value = path.value.toUpperCase(Locale.ROOT)
            if (value.isEmpty()) {
                continue
            }
            val type = path.type
            //如果是创建表语句
            if (type == TypeConfig.TYPE_DB_CREATE_TABLE) {
                constructor.addStatement("tables$index.add(%S)", value)
            }
            //数据库版本
            if (type == TypeConfig.TYPE_DB_PATH) {
                dbPath = value
            }
            //数据库发生改变的表
            if (type == TypeConfig.TYPE_DB_TABLE_UPDATE) {
                updates = value
            }
            //数据库删除的表
            if (type == TypeConfig.TYPE_DB_TABLE_DELETE) {
                deletes = value
            }
        }
        // add method
        //$S for Strings:$S 表示一个 string
        //$T for Types:类型，通过 $T 进行映射，会自动import声明
        constructor
            .addStatement(
                "tablesMapping.put(%S,tables$index)", dbName
            )
            .addStatement(
                "versionsMapping.put(%S,%L)", dbName, dbVersion
            )
            .addStatement(
                "pathMapping.put(%S,%S)", dbName, dbPath
            ).addStatement(
                "updateTableMapping.put(%S,%S)", dbName, updates
            ).addStatement(
                "deleteTableMapping.put(%S,%S)", dbName, deletes
            )
        return constructor
    }

    /**
     * build fields
     */
    private fun buildRouterModuleFields(): Iterable<PropertySpec> {
        val fieldSpecs: ArrayList<PropertySpec> = ArrayList()
        val mapping = PropertySpec.builder(
            "tablesMapping",
            HashMap::class.parameterizedBy(String::class, Any::class),
            KModifier.FINAL
        )
            .build()
        fieldSpecs.add(mapping)
        val version = PropertySpec.builder(
            "versionsMapping",
            HashMap::class.parameterizedBy(String::class, Int::class),
            KModifier.FINAL
        )
            .build()
        fieldSpecs.add(version)
        val path = PropertySpec.builder(
            "pathMapping",
            HashMap::class.parameterizedBy(String::class, String::class),
            KModifier.FINAL
        )
            .build()
        fieldSpecs.add(path)
        val update = PropertySpec.builder(
            "updateTableMapping",
            HashMap::class.parameterizedBy(String::class, String::class),
            KModifier.FINAL
        )
            .build()
        fieldSpecs.add(update)
        val delete = PropertySpec.builder(
            "deleteTableMapping",
            HashMap::class.parameterizedBy(String::class, String::class),
            KModifier.FINAL
        )
            .build()
        fieldSpecs.add(delete)
        return fieldSpecs
    }

    companion object {
        const val FILE_DOC = "DO NOT EDIT THIS FILE!!! IT WAS GENERATED BY ANDROID ROUTER."
        const val PACKAGE_NAME = "com.lebusishu.db"
        const val PREFIX = "kt_auto_"
    }
}