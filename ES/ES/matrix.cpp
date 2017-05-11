#include "matrix.h"

neural_network::matrix::matrix(): height(0), width(0)
{

}

neural_network::matrix::matrix(const size_t height, const size_t width): height(height), width(width)
{
	if (height <= 0 || width <= 0)
		throw std::logic_error("in matrix::matrix(const size_t, const size_t) logic error with negative matrix dimentions");

	data = new double*[height];
	for (int i = 0; i < height; i++)
	{
		data[i] = (double*)calloc(width, sizeof(double));
	}
}

double* neural_network::matrix::operator[](const size_t index)
{
	return data[index];
}

const double* neural_network::matrix::operator[](const size_t index) const
{
	return data[index];
}

neural_network::matrix neural_network::matrix::operator*(const matrix & right) const
{
	if (this->width != right.height)
	{
		std::string message = "at neural_betwork::matrix::operator*(const matrix&) wrong multiplication expected ";
		message += this->width + " but was " + right.height;
		throw std::logic_error(message);
	}

	matrix result = matrix(this->height, right.width);

	//non-naiive way to multiply matrises
	double t;
	for (int i = 0; i < this->height; i++)
	{
		for (int k = 0; k < right.height; k++)
		{
			t = this->data[i][k];
			for (int j = 0; j < right.width; j++)
			{
				result[i][j] += t * right.data[k][j];
			}
		}
	}

	return result;
}

neural_network::matrix neural_network::matrix::operator*(const double right) const
{
	matrix result = matrix(*this);

	return result.for_each([&](double value) { return value*right; });
}

neural_network::matrix neural_network::matrix::operator+(const matrix & right) const
{
	if (this->height != right.height || this->width != right.width)
	{
		throw std::logic_error("at neural_network::matrix::operator+(const matrix&) logic error, missmatch of dimensions");
	}

	matrix result = matrix(*this);

	for (int i = 0; i < this->height; i++)
	{
		for (int j = 0; j < this->width; j++)
		{
			result[i][j] += right[i][j];
		}
	}

	return result;
}



neural_network::matrix neural_network::matrix::for_each(std::function<double (double)> p)
{
	for (int i = 0; i < height;i++)
	{
		for (int j = 0; j < width;j++)
		{
			data[i][j] = p(data[i][j]);
		}
	}

	return *this;
}

neural_network::matrix neural_network::matrix::rand_init(const size_t height, const size_t width, const int seed)
{
	matrix result = matrix(height, width);

	srand(seed);

	return result.for_each([](double value) {return double(rand()) / (double)RAND_MAX; });
}

neural_network::matrix::~matrix()
{
	for (int i = 0; i < height; i++)
		delete[] data[i];

	delete[] data;
}
