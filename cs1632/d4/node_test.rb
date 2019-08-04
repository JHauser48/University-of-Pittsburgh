require 'minitest/autorun'
require_relative 'graph_reader'

class NodeTest < Minitest::Test 

  def test_node_letter
  	node = Node.new(1, 'a', [])
  	assert_equal 1, node.number
  end

  def test_node_num
  	node = Node.new(1, 'a', [])
  	assert_equal 'a', node.letter
  end

  def test_node_neighbors
  	node = Node.new(1, 'a', [2, 3])
  	assert_equal [2, 3], node.neighbors
  end

  def test_no_neighbors
    node = Node.new(1, 'a', nil)
    assert_nil node.neighbors
  end

  def test_print_node
    node = Node.new(1, 'a', [2, 3])
    assert_output("\nNumber: 1\nLetter: a\nNeighbors: [2, 3]\n") {node.print_node()} 
  end

  def test_print_node_nil_neighbors
    node = Node.new(1, 'a', nil)
    assert_output("\nNumber: 1\nLetter: a\nNeighbors: none\n") {node.print_node()} 
  end 	 	
end