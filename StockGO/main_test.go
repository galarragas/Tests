package main

import "testing"

var point1 = Point{1, 20}
var point2 = Point{2, 30}
var point3 = Point{3, 40}
var point4 = Point{4, 10} //Min
var point5 = Point{5, 20}
var point6 = Point{6, 25}
var point7 = Point{7, 35} //Max profit selling
var point8 = Point{8, 10}
	
var interval1 = Interval{&point1, &point2}
var interval2 = Interval{&point1, &point3}

var emptyList = []Point{}
var growingList = []Point{point1, point2, point3}
var fullList = []Point{point1, point2, point3, point4, point5, point6, point7, point8}
		

func TestMin(t *testing.T) { 
 	assertEqual(t, point1, *min(&point1, &point1))
}

func TestMostProfitable(t *testing.T) {
    assertEqual(t, interval2, *mostProfitable(&interval1, &interval2)) 
}

func TestCurrMaxNoStatus(t *testing.T) {
    assertEqual(t, Status { &Interval {&point1, &point1}, &point1 }, *currMax(nil, &point1) )
}

func TestCurrMaxFirstPoint(t *testing.T) {
    assertEqual(t, Status { &Interval {&point1, &point1}, &point1 }, *currMax(nil, &point1) )
}

func TestCurrMaxIncreasingChangesMaxInOptimum(t *testing.T) {
    assertEqual(t, Status { &Interval {&point1, &point2}, &point1 }, *currMax(&Status { &Interval {&point1, &point1}, &point1 }, &point2) )
}

func TestCurrMaxDecreasingChangesMin(t *testing.T) {
    assertEqual(t, Status { &Interval {&point1, &point2}, &point4 }, *currMax(&Status { &Interval {&point1, &point2}, &point1 }, &point4) )
}

func TestMaxOnEmptyListIsNil(t *testing.T) {
	maxVal := max(emptyList)
	if maxVal != nil {
		t.Errorf("Expecting nil result but got %s", maxVal.print())	
	}
}

func TestMaxOnGrowingListReturnsExtremes(t *testing.T) {
	assertEqual( t, Interval {&point1, &point3}, *max(growingList) )
	
}

func TestMaxWorksWithComplexList(t *testing.T) {
	assertEqual( t, Interval {&point4, &point7}, *max(fullList) )
	
}

func TestReadPointsFromCSV(t *testing.T) {
	points := readPointsFromCSV("test.csv")
	
	if len(points) != len(fullList) {
		t.Errorf("Result has wrong length expected %d but found %d", len(fullList), len(points))
	}
	
	for i := 0; i < len(points); i++ {
		assertEqual(t, fullList[i], points[i])
	} 
}

type Assertable interface {
	equal(that Comparable) bool	
	print() string
}

func assertEqual(t *testing.T, expected Assertable, actual Assertable) {
	if !expected.equal(actual) {
		t.Errorf("Objects are not equal, expected %s but found %s", expected.print(), actual.print())
	}
}

