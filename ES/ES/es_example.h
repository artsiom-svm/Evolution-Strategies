#pragma once
#include "local_includes.h"
#include "es.h"

class es_example
{
	//size fo population
	static constexpr size_t npop = 50;
	//noise standard devivation
	static constexpr double sigma = 0.1;
	//learing rate
	static constexpr double alpha = 0.001;

	//the truth solution to the problem
	std::vector<double> solution;
	//current best guess
	std::vector<double> tries;

	double mean(const std::vector<double> set) const;
	double std(const std::vector<double> set) const;
public:

	double mean_square_error(const std::vector<double> guess) const;
	es_example(const std::vector<double> solution);
	void evolve();
	std::vector<double> getSolution() const;
	std::vector<double> getApproximation() const;
	~es_example();
};

