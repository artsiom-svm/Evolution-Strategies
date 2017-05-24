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

void print(double* data, size_t size)
{
	std::cout << "[ ";
	for (size_t i = 0; i < size; i++)
		std::cout << data[i] << " ";
	std::cout << "]\n";
}

int main1()
{
	const size_t size = 66;
	es::es* es = new es::es(size);

	print(es->get_current_guess(), size);
	for (int i = 0; i < 300; i++)
	{
		es->evolve();
		if(i%20==0)
		print(es->get_current_guess(), size);
		//std::cin.get();
	}

	delete es;
	
	std::cin.get();
	return 0;
}

int main()
{

	std::vector<es_example> v;

	try {
		es::es es(3);
		es.evolve();

		/*
		std::cout << "Using ML ES:\n";
		for (int i = 0; i <= 300; i++)
		{
			if (i % 100 == 0)
			{
				std::cout << "iter " << i << ": ";
				print(es.get_current_guess(),3);
				std::cout << " w\\ stderr: " << es.f(es.get_current_guess()) << std::endl;
			}
			es.evolve();

			//t.output_data("", i);
		}
		return 0;
		*/

		
		es_example t(66);	

		std::cout << "Using ML ES:\n";
		for (int i = 0;; i++)
		{
			if (i % 50 == 0)
			{
				std::cout << "iter " << i << ": " << " w\\ stderr: " << t.f(t.getApproximation())<< std::endl;
			}
			t.evolve();
			
			t.output_data("", i);
		}

	}
	catch (std::exception e) {
		std::cerr << e.what();
	}
	
	system("pause");
	return 0;
}