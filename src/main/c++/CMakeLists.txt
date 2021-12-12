cmake_minimum_required(VERSION 3.20)
project(GD_Basic_RunTime)

set(CMAKE_CXX_STANDARD 14)

configure_file(version.h.in version.h)

add_executable(GD_Basic_RunTime main.cpp util/libutil.a)

