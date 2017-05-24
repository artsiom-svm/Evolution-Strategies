#include "matrix.h"

neural_network::matrix::matrix(const matrix & copy):height(copy.height), width(copy.width)
{
	this->data = new double*[height];
	
	for (size_t i = 0; i < height; i++)
	{
		this->data[i] = new double[width];
		for (size_t j = 0; j < width; j++)
		{
			this->data[i][j] = copy[i][j];
		}
	}
}

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

neural_network::matrix::matrix(const double ** data, const size_t height, const size_t width): height(height), width(width)
{
	this->data = new double*[height];

	for (size_t i = 0; i < height; i++)
	{
		this->data[i] = new double[width];
		for (size_t j = 0; j < width; j++)
		{
			this->data[i][j] = data[i][j];
		}
	}
}

neural_network::matrix::matrix(const double * data, const size_t height, const size_t width): height(height), width(width)
{
	this->data = new double*[height];
	size_t index = 0;
	for (size_t i = 0; i < height; i++)
	{
		this->data[i] = new double[width];
		for (size_t j = 0; j < width; j++)
		{
			this->data[i][j] = data[index++];
		}
	}
}

neural_network::matrix::matrix(const std::function<double()> p, const size_t height, const size_t width): height(height), width(width)
{
	this->data = new double*[height];

	for (size_t i = 0; i < height; i++)
	{
		this->data[i] = new double[width];
		for (size_t j = 0; j < width; j++)
			this->data[i][j] = p();
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

	return *result.for_each([&](double value) { return value*right; });
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

neural_network::matrix neural_network::matrix::operator-(const matrix & right) const
{
	if (this->height != right.height || this->width != right.width)
	{
		throw std::logic_error("at neural_network::matrix::operator+(const matrix&) logic error, missmatch of dimensions");
	}

	matrix m(*this);
	for (size_t i = 0; i < height; i++)
	{
		for (size_t j = 0; j < width; j++)
		{
			m[i][j] -= right[i][j];
		}
	}

	return m;
}

neural_network::matrix & neural_network::matrix::operator+=(const matrix & right)
{
	for (size_t i = 0; i < height; i++)
		for (size_t j = 0; j < width; j++)
			data[i][j] += right[i][j];

	return *this;
}

neural_network::matrix* neural_network::matrix::for_each(const std::function<double (double)> p)
{
	for (int i = 0; i < height;i++)
	{
		for (int j = 0; j < width;j++)
		{
			data[i][j] = p(data[i][j]);
		}
	}

	return this;
}

neural_network::matrix neural_network::matrix::transpose()
{
	matrix answ(width, height);

	for (size_t i = 0; i < width; i++)
		for (size_t j = 0; j < height; j++)
			answ[i][j] = data[j][i];

	return answ;
}

neural_network::matrix::operator double*() const
{
	size_t index = 0;
	double* array = new double[height*width];
	for (size_t i = 0; i < height; i++)
		for (size_t j = 0; j < width; j++)
			array[index++] = data[i][j];

	return array;
}

const size_t neural_network::matrix::get_height() const
{
	return this->height;
}

const size_t neural_network::matrix::get_width() const
{
	return this->width;
}

neural_network::matrix neural_network::matrix::rand_init(const size_t height, const size_t width, const int seed)
{
	srand(seed);
	
	matrix m(height, width);

	return *m.for_each([&](double d) { return double(rand()) / (double)RAND_MAX; });
}

neural_network::matrix::~matrix()
{
	for (int i = 0; i < height; i++)
		delete[] data[i];

	delete[] data;
}

std::ostream & neural_network::operator<<(std::ostream & os,const matrix & m)
{
	os.precision(2);
	for (size_t h = 0; h < m.height; h++)
	{
		os << "|";
		for (size_t w = 0; w < m.width; w++)
		{
			os << m[h][w] << " |";
		}
		os << std::endl;
	}

	return os;
}
