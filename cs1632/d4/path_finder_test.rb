require 'minitest/autorun'
require_relative 'path_finder'
require_relative 'graph_reader'

# Test path finder
class RunPathFindeTest < Minitest::Test
  def test_initialize
    # gr = GraphReader.new('small_graph.txt')
    mocked_gr = Minitest::Mock.new('mocked')
    def mocked_gr.graph
      'abc'
    end
    pf = PathFinder.new(mocked_gr)
    assert_equal pf.instance_variable_get(:@graph), 'abc'
  end

  def test_get_paths_nil
    gr = GraphReader.new('small_graph.txt')
    node = Node.new(1, 'a', [])
    pf = PathFinder.new(gr)
    assert_equal pf.get_paths(node), []
  end

  def test_get_paths_not_nil
    gr = GraphReader.new('small_graph.txt')
    node = Node.new(1, 'a', nil)
    pf = PathFinder.new(gr)
    assert_equal pf.get_paths(node), 'a'
  end

  def test_find_all_paths
    gr = GraphReader.new('small_graph.txt')
    pf = PathFinder.new(gr)
    pf.find_all_paths
    assert_nil @graph
  end

  def test_start_search
    gr = GraphReader.new('small_graph.txt')
    pf = PathFinder.new(gr)
    node = Node.new(1, 'a', [])
    pf.start_search(node)
    assert_nil @path
  end

  def test_search_path_nil
    gr = GraphReader.new('small_graph.txt')
    pf = PathFinder.new(gr)
    node1 = Node.new(1, 'a', [])
    node2 = Node.new(2, 'b', [])
    pf.search_path(node1, node2)
    assert_nil @path
  end

  def test_search_path_not_nil
    gr = GraphReader.new('small_graph.txt')
    pf = PathFinder.new(gr)
    node1 = Node.new(1, 'a', nil)
    node2 = Node.new(2, 'b', nil)
    pf.search_path(node1, node2)
    assert_nil @path
  end

  def test_print_paths
    gr = GraphReader.new('small_graph.txt')
    pf = PathFinder.new(gr)
    assert_output(//) { pf.print_paths }
  end
end