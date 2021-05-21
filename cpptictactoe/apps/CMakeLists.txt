add_executable(play play.cc)
target_compile_features(play PRIVATE cxx_std_17)
target_link_libraries(play PRIVATE tictactoe)
