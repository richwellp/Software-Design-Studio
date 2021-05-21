# Tests need to be added as executables first
add_executable(test-snake test_snake.cc)

# We're using C++14 in the test
target_compile_features(test-snake PRIVATE cxx_std_14)

# Should be linked to the main library, as well as the Catch2 testing library
target_link_libraries(test-snake PRIVATE snake catch2)

# If you register a test, then ctest and make test will run it.
# You can also run examples and check the output, as well.
add_test(NAME test-snake COMMAND test-snake) # Command can be a target

# Add folders
set_target_properties(test-snake PROPERTIES FOLDER cs126)

set_property(TARGET test-snake PROPERTY
        MSVC_RUNTIME_LIBRARY "MultiThreaded$<$<CONFIG:Debug>:Debug>")

# Cross-platform compiler lints
if (${CMAKE_CXX_COMPILER_ID} STREQUAL "Clang"
        OR ${CMAKE_CXX_COMPILER_ID} STREQUAL "GNU")
    target_compile_options(test-snake PRIVATE
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
    target_compile_options(test-snake PRIVATE
            /W3)
endif ()


add_custom_command(
        TARGET test-snake
        PRE_BUILD
        COMMAND ${CMAKE_COMMAND} -DSRC_DIR="${CMAKE_CURRENT_SOURCE_DIR}/data" -DDEST_DIR="${CMAKE_CURRENT_BINARY_DIR}/data" -P "${CMAKE_SOURCE_DIR}/cmake/configure_files.cmake"
)

