CMAKE_MINIMUM_REQUIRED(VERSION 3.4.1)
#设置项目的名字
PROJECT(icucompat C CXX)
#将${ICU_COMPAT_DIR}目录下的所有源文件找到并且赋值给ICU_COMPAT_DIR变量
AUX_SOURCE_DIRECTORY(${ICU_COMPAT_DIR} ICU_COMPAT_DIR)
#将目录中加入搜索头文件时的路径
INCLUDE_DIRECTORIES(${ICU_COMPAT_DIR})
INCLUDE_DIRECTORIES(unicode)
#[[
设置C编译参数，和add_compile_options一样
1,add_compile_options命令添加的编译选项是针对所有编译器的(包括c和c++编译器)，
2,而set命令设置CMAKE_C_FLAGS或CMAKE_CXX_FLAGS变量则是分别只针对c和c++编译器的
]]
SET(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -D_REENTRANT \
-Wall -Werror -ffunction-sections -fdata-sections \
-DSQLITE_HAS_CODEC -DSQLITE_CORE -DSQLITE_OS_UNIX")

SET(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -DPIC -fPIC")
#添加一个库文件，类型为STATIC：静态库
ADD_LIBRARY(icucompat STATIC ${ICU_COMPAT_DIR})