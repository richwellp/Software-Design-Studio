file(GLOB SOURCE_LIST CONFIGURE_DEPENDS
        "${LinkedList_SOURCE_DIR}/tests/*.h"
        "${LinkedList_SOURCE_DIR}/tests/*.hpp"
        "${LinkedList_SOURCE_DIR}/tests/*.cc"
        "${LinkedList_SOURCE_DIR}/tests/*.cpp")

# This target runs all of the tests.
# Make sure CATCH_CONFIG_MAIN is only defined in one test file.
add_executable(test-cs126linkedlist ${SOURCE_LIST})

# We're using C++17 in the test
target_compile_features(test-cs126linkedlist PRIVATE cxx_std_17)

# Should be linked to the main library, as well as the Catch2 testing library
target_link_libraries(test-cs126linkedlist PRIVATE cs126linkedlist catch2)

# If you register a test, then ctest and make test will run it.
# You can also run examples and check the output, as well.
add_test(NAME test-cs126linkedlist COMMAND test-cs126linkedlist) # Command can be a target

# Add folders
set_target_properties(test-cs126linkedlist PROPERTIES FOLDER cs126)

# Cross-platform compiler lints
if (${CMAKE_CXX_COMPILER_ID} STREQUAL "Clang"
        OR ${CMAKE_CXX_COMPILER_ID} STREQUAL "GNU")
    target_compile_options(test-cs126linkedlist PRIVATE
            -Wall
            -Wextra
            -Wswitch
            -Wconversion
            -Wparentheses
            -Wfloat-equal
            -Wzero-as-null-pointer-constant
            -Wpedantic
            -pedantic
            -pedantic-errors)
elseif (${CMAKE_CXX_COMPILER_ID} STREQUAL "MSVC")
    target_compile_options(test-cs126linkedlist PRIVATE
            /W3)
endif ()

