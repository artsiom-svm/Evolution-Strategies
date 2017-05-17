#include "es_example.h"


double es_example::mean_square_error(const std::vector<double> guess) const
{
	/*
	double result = 0;
	
	for (int i = 0; i < solution.size(); i++)
		result += (solution[i] - guess[i]) * (solution[i] - guess[i]);

	return -result;
	*/


	jclass cls = env->FindClass("Main");
	if (cls == nullptr)
	{
		std::cerr << "ERROR: class not found!";
		std::cin.get();
		exit(EXIT_FAILURE);
	}

	//create java double array
	//6*6 + 5*6
	size_t size = 66;
	jdoubleArray array = env->NewDoubleArray(size);
	jdouble* body = env->GetDoubleArrayElements(array, 0);

	for (size_t i = 0; i < size; i++)
	{
		body[i] = guess[i];
	}

	env->ReleaseDoubleArrayElements(array, body, 0);

	jmethodID id = env->GetMethodID(cls, "es_score", "([D)D");
	if (id == nullptr)
	{
		std::cerr << "ERROR: Id not found!";
		std::cin.get();
		exit(EXIT_FAILURE);
	}

	double result = env->CallStaticDoubleMethod(cls, id, array);
	
	return result;
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

void es_example::runVM()
{
	//================== prepare loading of Java VM ============================

	JavaVMInitArgs vm_args;                        // Initialization arguments

	JavaVMOption* options = new JavaVMOption[1];   // JVM invocation options

	options[0].optionString = "-Djava.class.path=.";   // where to find java .class

	options[0].extraInfo = 0;

	vm_args.version = JNI_VERSION_1_6;             // minimum Java version

	vm_args.nOptions = 1;                          // number of options

	vm_args.options = options;
	vm_args.ignoreUnrecognized = false;     // invalid options make the JVM init fail

											//=============== load and initialize Java VM and JNI interface =============

	jint rc = JNI_CreateJavaVM(&jvm, (void**)&env, &vm_args);  // YES !!

	delete options;    // we then no longer need the initialisation options. 

	if (rc != JNI_OK) {
		// TO DO: error processing... 

		std::cin.get();
		exit(EXIT_FAILURE);
	}
	//=============== Display JVM version =======================================

	std::cout << "JVM load succeeded: Version ";
	jint ver = env->GetVersion();
	std::cout << ((ver >> 16) & 0x0f) << "." << (ver & 0x0f) << std::endl;

}

es_example::es_example(const std::vector<double> solution)
{
	runVM();

	this->solution = solution;
	
	for (auto& d : solution)
		tries.push_back(double(rand()) / (double)RAND_MAX);
}

es_example::es_example(const size_t size)
{
	runVM();
	this->solution = std::vector<double>(size);

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
