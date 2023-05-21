cmake_minimum_required(VERSION 3.20)
project(GD_Basic_RunTime)

set(CMAKE_CXX_STANDARD 14)
add_library( libutil.a STATIC IMPORTED )
set_target_properties( libutil.a PROPERTIES IMPORTED_LOCATION ../../../main/c++/util/libutil.a )
add_executable(TestSystemUtils TestSystemUtils.cpp)
TARGET_LINK_LIBRARIES(TestSystemUtils libutil.a)

