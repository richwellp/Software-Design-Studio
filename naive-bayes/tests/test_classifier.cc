// Copyright (c) 2020 [Your Name]. All rights reserved.

#define CATCH_CONFIG_MAIN

#include <bayes/classifier.h>

#include <catch2/catch.hpp>

TEMPLATE_TEST_CASE("Check changes in data", "[training][template]", int,
                   std::string, (std::tuple<int, float>)) {
  std::string images = "data/trainingimages";
  std::string labels = "data/traininglabels";
  std::string model = "data/model.json";
  // set up
  bayes::Classifier trainer(images, labels);
  trainer.Train();
  SECTION("Inspect training behavior") {
    bayes::Model probability_model = trainer.GetProbabilityModel();
    int label = 4;
    REQUIRE(probability_model.GetPrior(label) == ((double)1 / 3));
    label = 9;
    REQUIRE(probability_model.GetPrior(label) == 0);
    int row = 9;
    int col = 8;
    label = 5;
    int shade = 1;
    REQUIRE(probability_model.GetProbability(row, col, label, shade) != 0);
    REQUIRE(probability_model.GetTrainingCount() == 3);
    REQUIRE(probability_model.GetPrior(label) == ((double)1 / 3));
  }
  SECTION("Inspect calculations in posterior probability") {
    images = "tests/data/sampleimages";
    labels = "tests/data/samplelabels";
    bayes::Classifier trainer2(images, labels);
    trainer2.Train();
    bayes::Classifier classifier(images, labels, model);
    classifier.Classify();
    bayes::Model probability_model = classifier.GetProbabilityModel();
    int label = 5;
    REQUIRE(probability_model.GetPosteriorProbability(label) != 0);
    label = 7;
    REQUIRE(probability_model.GetPosteriorProbability(label) == 0);
  }
}
