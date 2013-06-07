package main

import ( 
	"fmt"
	"os"
	)
	
func main() {
	fmt.Printf("Reading from %s\n", os.Args[1])
	stockPriceGraph := readPointsFromCSV(os.Args[1])
	
	maxInterval := max(stockPriceGraph)
	
	fmt.Printf("Max is %s with a profit of %d\n", printInterval(maxInterval), profit(maxInterval))
}
