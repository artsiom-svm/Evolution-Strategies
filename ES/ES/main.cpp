#include "local_includes.h"

#include <iostream>
#include <ctime>
#include "matrix.h"
#include "es_example.h"

std::ostream& operator<<(std::ostream& os, const std::vector<double>& set)
{
	os << '[';
	for (auto d : set)
		os << d << ' ';
	os << ']';
	return os;
}

int main()
{
	try {
		//example of es on line vector
		std::vector < double> solution = { 2.5, 0.1, -10.3 };
		es_example t(solution);

		std::cout << "Real solution : " << solution << std::endl;

		std::cout << "Using ML ES:\n";
		for (int i = 0; i < 300; i++)
		{
			t.evolve();
			if (i % 20 == 0)
				std::cout << "iter " << i << ": " << t.getApproximation() << " w\\ stderr: " << t.mean_square_error(t.getApproximation())<< std::endl;
		}

	}
	catch (std::exception e) {
		std::cerr << e.what();
	}
	
	system("pause");
	return 0;
}