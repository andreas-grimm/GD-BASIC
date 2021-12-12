cmake_minimum_required(VERSION 3.9)
project(util VERSION 1.0.1 DESCRIPTION "System Utilities")
include(GNUInstallDirs)
add_library(util STATIC SystemUtils.cpp)
set_target_properties(util PROPERTIES
        VERSION ${PROJECT_VERSION}
        SOVERSION 1)
configure_file(util.pc.in util.pc @ONLY)
target_include_directories(util PRIVATE .)
install(TARGETS util
        LIBRARY DESTINATION ${CMAKE_INSTALL_LIBDIR}
        PUBLIC_HEADER DESTINATION ${CMAKE_INSTALL_INCLUDEDIR})
install(FILES ${CMAKE_BINARY_DIR}/util.pc
        DESTINATION ${CMAKE_INSTALL_DATAROOTDIR}/pkgconfig)