# small class for creating graph nodes
class Node
  attr_accessor :number, :letter, :neighbors

  def initialize(number, letter, neighbors)
    @letter = letter
    @number = number
    @neighbors = neighbors
  end

  def print_node
    print "\nNumber: "
    print @number
    puts "\nLetter: " + @letter
    print 'Neighbors: '
    print neighbors unless neighbors.nil?
    print 'none' if neighbors.nil?
    print "\n"
  end
end
