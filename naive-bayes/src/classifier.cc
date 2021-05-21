// Copyright 2020 [Your Name]. All rights reserved.

#include <bayes/classifier.h>
#include <bayes/image.h>
#include <bayes/model.h>

#include <iostream>
#include <nlohmann/json.hpp>

namespace bayes {
Classifier::Classifier() = default;
Classifier::Classifier(std::string& images_path, std::string& labels_path) {
  labels = labels_path;
  images = images_path;
}
Classifier::Classifier(std::string& images_path, std::string& labels_path,
                       std::string& model_path) {
  labels = labels_path;
  images = images_path;
  model = model_path;
}
void Classifier::Train() {
  std::ifstream training_images;
  training_images.open(images, std::ifstream::in);
  std::ifstream training_labels;
  training_labels.open(labels, std::ifstream::in);
  // check if file could not open
  if (!training_images || !training_labels) {
    std::cout << "Error: file(s) is invalid." << std::endl;
    return;
  }
  // read through the files and calculate the probabilities
  std::string line;
  bayes::Image image{};
  int data_count = 0;
  int row = 0;  // line count (0-27) or row index for the image array
  while (std::getline(training_images, line) && training_labels.good()) {
    for (int column = 0; column < line.size(); ++column) {
      image.SetPixel(row, column, line.at(column));
    }
    // updates the data count if the image is completely stored in the 2D array
    if (row == bayes::kImageSize - 1) {
      data_count++;
      row = 0;                   // reset for the next image
      char label;                // or called class in the probability context
      training_labels >> label;  // get the character label in the line
      int class_number = label - '0';  // converts a character to int ('0' to 0)
      // collect relevant data for the model
      probability_model.UpdateOccurrences(class_number, image);
      probability_model.AddClassCount(class_number);
    } else {
      row++;
    }
  }
  probability_model.SetTrainingCount(data_count);
  probability_model.CalculateProbabilities();
  // Generate a JSON file
  probability_model.GenerateJSON();
  training_images.close();
  training_labels.close();
  std::cout << "Training finished." << std::endl;
}
void Classifier::Classify() {
  std::ifstream test_images;
  test_images.open(images, std::ifstream::in);
  std::ifstream test_labels;
  test_labels.open(labels, std::ifstream::in);
  std::ifstream model_file;
  model_file.open(model, std::ifstream::in);
  // check if file could not open
  if (!test_images || !test_labels || !model_file) {
    std::cout << "Error: file(s) is invalid." << std::endl;
    return;
  }
  ReadJSON(model);
  // read through the files and calculate the probabilities
  std::string line;
  bayes::Image image{};
  int line_count = 0;  // line count (0-27) or row index for the image array
  while (std::getline(test_images, line) && test_labels.good()) {
    for (int column = 0; column < line.size(); ++column) {
      image.SetPixel(line_count, column, line.at(column));
    }
    // evaluate the posterior probability
    if (line_count == bayes::kImageSize - 1) {
      line_count = 0;
      char label;
      test_labels >> label;
      for (int class_number = 0; class_number < kNumClasses; ++class_number) {
        // computation of the posterior probability
        double prob = log(probability_model.GetPrior(class_number));
        for (int row = 0; row < kImageSize; ++row) {
          for (int col = 0; col < kImageSize; ++col) {
            if (image.GetPixel(row, col) == '+' ||
                image.GetPixel(row, col) == '#') {
              int shade = 0;
              prob += log(probability_model.GetProbability(
                  row, col, class_number, shade));
            } else {
              int shade = 1;
              prob += log(probability_model.GetProbability(
                  row, col, class_number, shade));
            }
          }
        }
        probability_model.SetPosteriorProbability(class_number, prob);
        std::cout << "Label: " << label << std::endl;
        std::cout << probability_model << std::endl;
      }
    } else {
      line_count++;
    }
  }
  test_images.close();
  test_labels.close();
  model_file.close();
  std::cout << "Classifying is finished." << std::endl;
}
void Classifier::ReadJSON(std::string& model_path) {
  std::ifstream my_file(model_path);
  nlohmann::json model_json = nlohmann::json::parse(my_file);
  std::string class_name = "class";
  for (int index = 0; index < kNumClasses; ++index) {
    class_name.append(std::to_string(index));
    double prior = model_json[class_name]["prior"];
    probability_model.SetPriors(index, prior);
    for (int row = 0; row < kImageSize; ++row) {
      for (int col = 0; col < kImageSize; ++col) {
        for (int shade = 0; shade < kNumShades; ++shade) {
          double prob = model_json[class_name]["probability"][row][col][shade];
          probability_model.SetProbability(row, col, index, shade, prob);
        }
      }
    }
    class_name = "class";
  }
}
Model& Classifier::GetProbabilityModel() { return probability_model; }
}  // namespace bayes

