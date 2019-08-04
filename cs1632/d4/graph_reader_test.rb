require 'minitest/autorun'
require_relative 'graph_reader'

class GraphReaderTest < Minitest::Test 

  # covereage test to ensure an empty graph is created 
  def test_init
  	reader = GraphReader.new('file')
  	assert_equal reader.graph, []
  end
  
  # Unit Tests for method create_graph
  # Equivalence classes:
  #  valid graph file 
  #   -(not checking specifics of the file itself as we assume a valid filename is a properly formatted graph file)
  #  invalid file 

  # uses the small graph file to verify that a proper graph is created
  def test_create_graph
  	node1 = Node.new(1, 'C', [2, 3])
  	node2 = Node.new(2, 'A', [3, 4, 6])
  	node3 = Node.new(3, 'K', [5])
  	node4 = Node.new(4, 'T', nil)
  	node5 = Node.new(5, 'E', nil)
  	node6 = Node.new(6, 'B', nil)
  	small_graph = [node1, node2, node3, node4, node5, node6]
  	reader = GraphReader.new('small_graph.txt')
  	reader.create_graph()
  	reader.graph.shift
  	assert_equal reader.graph.map(&:letter), small_graph.map(&:letter)
  end

  # tests create graph when the file is invalid
  def test_create_invalid_file
  	reader = GraphReader.new('invalid')
  	assert_output("Error: file not found\n") {reader.create_graph()}
  end

  # Unit Tests for method create_graph
  # Equivalence classes:
  #  valid number: returns the node at the given index of the graph
  #  invalid number: returns nil (should not happen during execution)

  # check that valid numbers return properly
  def test_get_node_valid
    reader = GraphReader.new('small_graph.txt')
    reader.create_graph()
    assert_equal reader.graph[1], reader.get_node(1)
  end

  # invalid node numbes return nil
  def test_get_node_invalid
    reader = GraphReader.new('small_graph.txt')
    reader.create_graph()
    assert_nil reader.get_node(-1) 
  end

  # Unit test for method add_node(number, letter, neighbor)
  # Equivalance classes:
  #  valid node
  #  invalid node (nil number or letter)
  #   -this really should not happen as add_node is only called when reading a file,
  #    and the file is only adding a node if it is a valid file
  
  # Adding a valid node to the graph
  def test_add_node
    reader = GraphReader.new('whatever')
    reader.add_node(0, 'A', [1])
    assert_equal reader.graph[0].letter, 'A'
  end

  # Adding an invalid node to the graph 
  def test_add_node_invalid
    reader = GraphReader.new('whatever')
    assert_nil reader.add_node(nil, nil, [])
  end

  # test for increased coverage, simply prints the graph array 
  def test_print
    reader = GraphReader.new('whatever')
    reader.add_node(0, 'A', nil)
    assert_output("\nNumber: 0\nLetter: A\nNeighbors: none\n") {reader.print_graph()}
  end
end