package com.lebusishu.sqlite

import java.util.*

object StringUtil {
    private const val TABLE = "那就比较好吧是呵斥呵斥功能和方法替换采访稿郭海春打断他回复红地毯染色然后到估计忙重复VG胡椒粉和长传冲吊刚打过好吃的" +
            "被拒jvjvvjvjgkhkvytdsts我们中国人爱天安门你们真讨厌昆仑山地方就居然被啊收到了付款就开车v你" +
            "lkjdfgsf来的客服经理sdf恶恶然后hkhlbbaoew删了可地方尽快拿出，" +
            "打发莫妮卡的好看迪卡侬，模拟参加考试的话付款计划客家话" +
            "的旧方法轮廓设计符合ioef，不能每次v了很快‘；了对方考虑【乐然里5" +
            "546654576u地方vu分别走访了asdkfghdugknmbcvkesrt" +
            "可能吧看龙剑打飞机了Kerr托v家0580【中长距离课程bfhgsir" +
            " dgklajrlt风格化反馈嘎哈ui人会认为分库分那边v看fgklsjeto" +
            "地方v奶茶铺i皮特人品trot软件哦然后刚好，从v你，bnhghjllks" +
            "开发的感觉到了法国尽量让他赶紧离开雷锋ljiowejiyrywjk6746vhcgsgnmjgndbbncg不喝酒挂号费胡椒粉和恢复桂城街道女方黄花大闺女" +
            "就好是几号发会话的交房还得各个安全让责任屁哦天也热现在在哪，是会九点一点体温散热器安徒生我QWERTYUI噢QWERTYUI欧帕斯的饭馆寒假快乐主线程VB你们" +
            "QWERTYUI欧派请按照我随心而动吃软饭是他刚毕业很纠结破解的饭馆汇金科技反倒是大法官汇金科技很过分的房间和规范的饭馆或或或或或或去玩儿推欧赔" +
            "QWERTYUI欧帕斯的饭馆寒假快乐门板是初学乍练可计划规范的撒门板是初学乍练可计划规范的撒破以有太热情诺邦股份从大V发的"
    private const val LEN = TABLE.length
    private val random = Random()
    fun randomString(): String {
        val scope = random.nextInt(LEN)
        if (scope == 0) {
            return randomString()
        }
        val sb = StringBuilder(scope)
        for (i in 0 until scope) {
            val index = random.nextInt(LEN)
            sb.append(TABLE[index])
        }
        return sb.toString()
    }
}