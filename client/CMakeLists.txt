cmake_minimum_required(VERSION 3.8)
project(newSPL)

find_package(Boost)
if (Boost_FOUND)
    include_directories(${Boost_INCLUDE_DIR})
endif()

set(CMAKE_CXX_STANDARD 11)

set(SOURCE_FILES Boost_Echo_Client/src/connectionHandler.cpp  Boost_Echo_Client/include/connectionHandler.h)

add_executable(client Boost_Echo_Client/src/echoClient.cpp ${SOURCE_FILES})