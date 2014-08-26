//
// Special Session on Real-Parameter Optimization at CEC-05
// Edinburgh, UK, 2-5 Sept. 2005
//
// Organizers:
//	Prof. Kalyanmoy Deb
//		deb@iitk.ac.in
//		http://www.iitk.ac.in/kangal/deb.htm
//	A/Prof. P. N. Suganthan
//		epnsugan@ntu.edu.sg
//		http://www.ntu.edu.sg/home/EPNSugan
//
// Java version of the org.uma.test functions
//
// Matlab reference code
//	http://www.ntu.edu.sg/home/EPNSugan
//
// Java version developer:
//	Assistant Prof. Ying-ping Chen
//		Department of Computer Science
//		National Chiao Tung University
//		HsinChu City, Taiwan
//		ypchen@csie.nctu.edu.tw
//		http://www.csie.nctu.edu.tw/~ypchen/
//
// Typical use of the org.uma.test functions in the Benchmark:
//
//		// Create a Benchmark object
// 		Benchmark theBenchmark = new Benchmark();
//		// Use the factory function call to create a org.uma.test function object
//		//		org.uma.test function 3 with 50 dimension
//		//		the object class is "TestFunc"
//		TestFunc aTestFunc = theBenchmark.testFunctionFactory(3, 50);
//		// Invoke the function with x
//		double experimentoutput = aTestFunc.f(x);
//
// Version 0.90
//		Currently, this version cannot handle any numbers of dimensions.
//		It cannot generate the shifted global optima and rotation matrices
//		that are not provided with the Matlab reference code.
//		It can handle all cases whose data files are provided with
//		the Matlab reference code.
// Version 0.91
//		Revised according to the Matlab reference code and the PDF document
//		dated March 8, 2005.
//
package org.uma.jmetal.problem.singleobjective.cec2005competitioncode;

import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;

public class F11_shifted_rotated_weierstrass extends TestFunc {

  // Fixed (class) parameters
  static final public String FUNCTION_NAME = "Shifted Rotated Weierstrass Function";
  static final public String DEFAULT_FILE_DATA =
    "" + JMetalLogger.cec2005SupportDataDirectory + "/weierstrass_data.txt";
  static final public String DEFAULT_FILE_MX_PREFIX =
    "" + JMetalLogger.cec2005SupportDataDirectory + "/weierstrass_M_D";
  static final public String DEFAULT_FILE_MX_SUFFIX = ".txt";

  static final public double PIx2 = Math.PI * 2.0;
  static final public int Kmax = 20;
  static final public double a = 0.5;
  static final public double b = 3.0;

  // Shifted global optimum
  private final double[] m_o;
  private final double[][] m_matrix;

  // In order to avoid excessive memory allocation,
  // a fixed memory buffer is allocated for each function object.
  private double[] m_z;
  private double[] m_zM;

  // Constructors
  public F11_shifted_rotated_weierstrass(int dimension, double bias) throws JMetalException {
    this(dimension, bias, DEFAULT_FILE_DATA,
      DEFAULT_FILE_MX_PREFIX + dimension + DEFAULT_FILE_MX_SUFFIX);
  }

  public F11_shifted_rotated_weierstrass(int dimension, double bias, String file_data,
    String file_m) throws JMetalException {
    super(dimension, bias, FUNCTION_NAME);

    // Note: dimension starts from 0
    m_o = new double[m_dimension];
    m_matrix = new double[m_dimension][m_dimension];

    m_z = new double[m_dimension];
    m_zM = new double[m_dimension];

    // Load the shifted global optimum
    Benchmark.loadRowVectorFromFile(file_data, m_dimension, m_o);
    // Load the matrix
    Benchmark.loadMatrixFromFile(file_m, m_dimension, m_dimension, m_matrix);
  }

  // Function body
  public double f(double[] x) {

    double result = 0.0;

    Benchmark.shift(m_z, x, m_o);
    Benchmark.xA(m_zM, m_z, m_matrix);

    result = Benchmark.weierstrass(m_zM);

    result += m_bias;

    return (result);
  }
}
