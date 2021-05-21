// Copyright 2020 [Your Name]. All rights reserved.

#include <bayes/model.h>

#include <fstream>
#include <iostream>
#include <nlohmann/json.hpp>

namespace bayes {
double &Model::GetProbability(int &row, int &col, int &class_number,
                              int &shade) {
  return probs_[row][col][class_number][shade];
}
void Model::SetProbability(int &row, int &col, int &class_number, int &shade,
                           double &prob) {
  probs_[row][col][class_number][shade] = prob;
}
double &Model::GetPrior(int &class_number) { return priors_[class_number]; }
void Model::SetPriors(int &class_number, double &prob) {
  priors_[class_number] = prob;
}
double &Model::GetPosteriorProbability(int &class_number) {
  return posterior_probability_[class_number];
}
void Model::SetPosteriorProbability(int &class_number, double &prob) {
  posterior_probability_[class_number] = prob;
}
void Model::CalculateProbabilities() {
  for (int row = 0; row < kImageSize; ++row) {
    for (int col = 0; col < kImageSize; ++col) {
      for (int class_number = 0; class_number < kNumClasses; ++class_number) {
        for (int shade = 0; shade < kNumShades; ++shade) {
          probs_[row][col][class_number][shade] =
              (kSmoothingFactor + occurrences_[row][col][class_number][shade]) /
              ((2 * kSmoothingFactor) + class_count_[class_number]);
        }
      }
    }
  }
  // calculate priors
  for (int class_number = 0; class_number < kNumClasses; ++class_number) {
    priors_[class_number] =
        (double)class_count_[class_number] / training_count_;
  }
}
int &Model::GetTrainingCount() { return training_count_; }
void Model::SetTrainingCount(int &count) { training_count_ = count; }
void Model::UpdateOccurrences(int &class_number, Image &image) {
  for (int row = 0; row < kImageSize; ++row) {
    for (int col = 0; col < kImageSize; ++col) {
      if (image.GetPixel(row, col) == '+' || image.GetPixel(row, col) == '#') {
        occurrences_[row][col][class_number][0]++;
      } else {
        occurrences_[row][col][class_number][1]++;
      }
    }
  }
}
void Model::AddClassCount(int &class_number) { class_count_[class_number]++; }
void Model::GenerateJSON() {
  nlohmann::json model;
  std::string class_name = "class";
  for (int index = 0; index < kNumClasses; ++index) {
    class_name.append(
        std::to_string(index));  // class_name would be "class0" to "class9"
    model[class_name]["prior"] = priors_[index];
    for (int row = 0; row < kImageSize; ++row) {
      for (int col = 0; col < kImageSize; ++col) {
        for (int shade = 0; shade < kNumShades; ++shade) {
          model[class_name]["probability"][row][col][shade] =
              probs_[row][col][index][shade];
        }
      }
    }
    class_name = "class";
  }
  // save the json object to the model file
  std::ofstream file("data/model.json");
  file << model;
  std::cout << "model file path is data/model.json" << std::endl;
  file.close();
}
std::ostream &operator<<(std::ostream &os, const Model &model) {
  os << "class"
     << "\t|\t"
     << "posterior probability" << std::endl;
  double highest = -DBL_MAX;  // values can be negative
  int classified = -1;
  for (int class_number = 0; class_number < kNumClasses; ++class_number) {
    os << class_number << "\t|\t" << model.posterior_probability_[class_number]
       << std::endl;
    if (model.posterior_probability_[class_number] > highest) {
      highest = model.posterior_probability_[class_number];
      classified = class_number;
    }
  }
  os << "The image is classified to be: " << classified << std::endl;
  return os;
}
}  // namespace bayes

