cmake_minimum_required(VERSION 3.20)
project(memoryManager VERSION 0.0.1 DESCRIPTION "C++ Memory Manager")
include(GNUInstallDirs)

set(CMAKE_CXX_STANDARD 14)

add_library(memoryManager Stack.cpp)

set_target_properties(memoryManager PROPERTIES
        VERSION ${PROJECT_VERSION}
        PUBLIC_HEADER include/memoryManager.h)

configure_file(include/version.h.in include/version.h @ONLY)
target_include_directories(memoryManager PRIVATE include)

install(TARGETS memoryManager
    LIBRARY DESTINATION ${CMAKE_INSTALL_LIBDIR}
    PUBLIC_HEADER DESTINATION ${CMAKE_INSTALL_INCLUDEDIR})

install(FILES ${CMAKE_BINARY_DIR}/memoryManager.pc
    DESTINATION ${CMAKE_INSTALL_DATAROOTDIR}/pkgconfig)
