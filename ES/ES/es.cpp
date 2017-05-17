#include "es.h"

double es::es::mean(const double * data) const
{
	double result = 0;
	for (size_t i = 0; i < es::npop; i++)
	{
		result += data[i];
	}

	return result / es::npop;
}

double es::es::std(const double * data) const
{
	double mean = this->mean(data);

	double* dev = new double[es::npop];
	
	for (size_t i = 0; i < es::npop; i++)
		dev[i] = (data[i] - mean) * (data[i] - mean);

	double result = sqrt(this->mean(dev));
	
	delete[] dev;
	return result;
}

double es::es::f(const double * guess) const
{
	
	double solution[] = { 0.5, 0.2, -0.3 };

	double award = 0;
	for (int i = 0; i < 3; i++)
	{
		award -= (guess[i] * solution[i]) * (guess[i] * solution[i]);
	}
	return award;
}

void es::es::evolve()
{
	//create a sample set of tries
	double** n_tries = new double*[es::npop];
	//create learining error-means
	double* R = new double[es::npop];
	//normalized learning vector
	double* A = new double[es::npop];

	//normal random N(0,1)
	std::default_random_engine generator;
	std::normal_distribution<double> distribution(0, 1);

	//creates npop random guesses to solution
	for (size_t i = 0; i < es::npop; i++)
	{
		n_tries[i] = new double[data_size];
		
		for (int j = 0; j < data_size; j++)
		{
			n_tries[i][j] = distribution(generator);
		}
	}


	//temp assignment
	//create new guess solution
	double* w_try = new double[data_size];
	
	//iterate award for random generated deviation from current guess
	for (size_t i = 0; i < es::npop; i++)
	{

		//add noise deviation from random generated matrix
		for (size_t j = 0; j < data_size; j++)
		{
			w_try[j] = current_guess[j] + es::sigma * n_tries[i][j];
		}

		//calculate award for new guess
		R[i] = f(w_try);
	}
	//free memory from temp assignment
	delete[] w_try;

	//normalizy learing vector
	auto mean = this->mean(R);
	auto std = this->std(R);
	for (size_t i = 0; i < es::npop; i++)
	{
		A[i] = (R[i] - mean) / std;
	}

	//update guess solution
	for (size_t i = 0; i < data_size; i++)
	{
		// dot = n_tries^T * A
		//dot product of noise and learning vector
		auto dot = 0;
		for (size_t j = 0; j < es::npop; j++)
		{
			dot += A[j] * n_tries[j][i];
		}

		//update of value
		current_guess[i] += es::alpha / (es::sigma * es::npop) * dot;
	}


	//free the memory
	for (size_t i = 0; i < es::npop; i++)
	{
		delete[] n_tries[i];
	}
	delete[] n_tries;
	delete[] R;
	delete[] A;
}

double * es::es::get_current_guess() const
{
	return current_guess;
}

es::es::es(const size_t size) : data_size(size)
{
	if (size <= 0)
		throw std::logic_error("ERROR: Trying to create model with non-positive size of data set\n");
	distribution = std::normal_distribution<double>(0, 1);

	current_guess = new double[data_size];
	for (size_t i = 0; i < data_size; i++)
	{
		current_guess[i] = distribution(generator);
	}
}

es::es::~es()
{
	delete[] current_guess;
}
