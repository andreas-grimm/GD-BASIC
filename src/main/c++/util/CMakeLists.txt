cmake_minimum_required(VERSION 3.20)
project(util VERSION 0.0.1 DESCRIPTION "C++ Utility Functions")
include(GNUInstallDirs)

set(CMAKE_CXX_STANDARD 14)

add_library(util SystemUtils.cpp)

set_target_properties(util PROPERTIES
        VERSION ${PROJECT_VERSION}
        PUBLIC_HEADER include/util.h)

configure_file(include/version.h.in include/version.h @ONLY)
target_include_directories(util PRIVATE include)

install(TARGETS util 
    LIBRARY DESTINATION ${CMAKE_INSTALL_LIBDIR}
    PUBLIC_HEADER DESTINATION ${CMAKE_INSTALL_INCLUDEDIR})

install(FILES ${CMAKE_BINARY_DIR}/util.pc
    DESTINATION ${CMAKE_INSTALL_DATAROOTDIR}/pkgconfig)
