// Copyright 2020 [Your Name]. All rights reserved.

#include <bayes/image.h>

namespace bayes {
char& Image::GetPixel(int& row, int& col) { return pixels_[row][col]; }
void Image::SetPixel(int& row, int& col, char& pixel) {
  pixels_[row][col] = pixel;
}
}  // namespace bayes

