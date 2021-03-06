package main

import (
	"strconv"
	"bufio"
	"encoding/csv"
	"os"
	"sort"
)

// By is the type of a "less" function that defines the ordering of its Planet arguments.
type By func(p1, p2 *Point) bool

type pointsSorter struct {
	points []Point
	by      By
}

// Len is part of sort.Interface.
func (s *pointsSorter) Len() int {
	return len(s.points)
}

// Swap is part of sort.Interface.
func (s *pointsSorter) Swap(i, j int) {
	s.points[i], s.points[j] = s.points[j], s.points[i]
}

// Less is part of sort.Interface. It is implemented by calling the "by" closure in the sorter.
func (s *pointsSorter) Less(i, j int) bool {
	return s.by(&s.points[i], &s.points[j])
}

func (by By) Sort(unsorted []Point) {
	ps := &pointsSorter { unsorted, by }
	
	sort.Sort(ps)	
}

func readPointsFromCSV(fileName string)  []Point {
	// open input file
    fi, err := os.Open(fileName)
    if err != nil { panic(err) }
    // close fi on exit and check for its returned error
    defer func() {
        if err := fi.Close(); err != nil {
            panic(err)
        }
    }()
	
	reader := bufio.NewReader(fi)
	
	csvReader := csv.NewReader(reader)
	
	records, err := csvReader.ReadAll() 
	
	if err != nil { panic(err) }
	
	var result = make([]Point, len(records))
	for i := 0; i < len(records); i++ {
		currRecord := records[i]
		x, err := strconv.Atoi(currRecord[0])
		if err != nil { panic(err) }
		y, err := strconv.Atoi(currRecord[1])
		if err != nil { panic(err) }
		
		result[i] = Point{ x, y } 	
	}
	
	sortByX := func (p1, p2 *Point) bool { 
		return p1.x < p2.x 
	}
	
	By(sortByX).Sort(result)
	
	return result
}

