require 'minitest/autorun'
require_relative 'word_find'

# Test word find
class RunWordFindTest < Minitest::Test
  def test_initialize
    wf = WordFind.new('abc')
    assert_equal wf.instance_variable_get(:@paths), 'abc'
  end

  def test_build_trie
    wf = WordFind.new('')
    wf.build_trie
    assert_equal true, wf.build_trie
  end

  def test_check_grammar_rules_q
    wf = WordFind.new('abc')
    assert_equal wf.check_grammar_rules('Q'), false
  end

  def test_check_grammar_rules_c
    wf = WordFind.new('abc')
    assert_equal wf.check_grammar_rules('C'), false
  end

  def test_check_grammar_rules_test
    wf = WordFind.new('abc')
    assert_equal wf.check_grammar_rules('TEST'), true
  end

  def test_check_grammar_rules_ttta
    wf = WordFind.new('abc')
    assert_equal wf.check_grammar_rules('TTTTTTTTTTTTTTTTTTTA'), false
  end

  def test_check_long_string
    wf = WordFind.new('abc')
    assert_equal false, wf.check_long_string(10, 'aaaaaaa')
  end

  def test_search_dict
    wf = WordFind.new('')
    wf.build_trie
    wf.search_dict('test')
    assert_equal wf.instance_variable_get(:@valid_words), ['test']
  end

  def test_find_longest_word
    wf = WordFind.new('')
    wf.build_trie
    wf.search_dict('add')
    wf.search_dict('send')
    wf.search_dict('leave')
    wf.find_longest_word
    assert_equal wf.instance_variable_get(:@max_words), ['leave']
  end

  def test_permute_paths
    wf = WordFind.new(['U'])
    wf.build_trie
    wf.permute_paths
    assert_equal wf.instance_variable_get(:@valid_words), ['U']
  end
end