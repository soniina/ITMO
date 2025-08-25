cmake_minimum_required(VERSION 3.28)
project(bmp_header_viewer_master C)

set(CMAKE_C_STANDARD 11)

include_directories(.)

add_executable(bmp_header_viewer_master
        bmp.c
        bmp.h
        main.c
        util.c
        util.h)
