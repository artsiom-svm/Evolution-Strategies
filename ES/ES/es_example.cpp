#include "es_example.h"


double es_example::mean_square_error(const std::vector<double> guess) const
{
	double result = 0;
	
	for (int i = 0; i < solution.size(); i++)
		result += (solution[i] - guess[i]) * (solution[i] - guess[i]);

	return -result;
	
}

double es_example::mean(const std::vector<double> set) const
{
	double sum = 0;
	for (auto& d : set)
		sum += d;
	return sum / set.size();
}

double es_example::std(const std::vector<double> set) const
{
	double mean = this->mean(set);
	
	std::vector<double> dev;

	for (auto& d : set)
		dev.push_back((d - mean)*(d - mean));

	return sqrt(this->mean(dev));
}

es_example::es_example(const std::vector<double> solution)
{
	this->solution = solution;
	
	for (auto& d : solution)
		tries.push_back(double(rand()) / (double)RAND_MAX);
}

void es_example::evolve()
{
	//create a sample set of tries
	std::vector<std::vector<double>> n_tries;
	//create learining error-means
	std::vector<double> R(es_example::npop);
	//normalized learning vector
	std::vector<double> A(es_example::npop);
	

	//normal random N(0,1)
	std::default_random_engine generator;
	std::normal_distribution<double> distribution(0, 1);


	//creates npop random guesses to solution
	for (size_t i = 0; i < es_example::npop; i++)
	{
		std::vector<double> ntry;
		for (size_t j = 0; j < this->tries.size(); j++)
		{
			ntry.push_back(distribution(generator));
		}
		n_tries.push_back(ntry);
	}

	//iterate random error-means
	for (size_t i = 0; i < es_example::npop; i++)
	{
		//create new guess matrix with noise deviation from current truth
		std::vector<double> w_try(n_tries[i].size());
		
		for (size_t j = 0; j < n_tries[i].size();j++)
		{
			w_try[j] = this->tries[j] + es_example::sigma * n_tries[i][j];
		}

		//push the learning rate from guess
		R[i] = mean_square_error(w_try);
	}

	//normalizy learing vector
	auto mean = this->mean(R);
	auto std = this->std(R);
	for (size_t i = 0; i < es_example::npop; i++)
	{
		A[i] = (R[i] - mean) / std;
	}

	//update of guess solution
	for (size_t i = 0; i < this->tries.size(); i++)
	{
		//dot product of noise and learning vector
		auto dot = 0;
		for (size_t j = 0; j < es_example::npop; j++)
		{
			dot += A[j] * n_tries[j][i];
		}

		//update of value
		tries[i] += es_example::alpha / (es_example::sigma * es_example::npop) * dot;
	}

}


std::vector<double> es_example::getSolution() const
{
	return solution;
}

std::vector<double> es_example::getApproximation() const
{
	return tries;
}

es_example::~es_example()
{
}
