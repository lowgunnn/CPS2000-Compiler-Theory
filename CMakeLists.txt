cmake_minimum_required(VERSION 3.15)
project(Compiler-Theory)

set(CMAKE_CXX_STANDARD 14)

set(SOURCE_FILES Lexer.cpp)
add_executable(Lexer ${SOURCE_FILES})

