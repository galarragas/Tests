// StockGO project main.go
package main

import "strconv"

type Comparable interface {
   equal(that Comparable) bool
}

type Printable interface {
   print() string
}

type Point struct {
    x int 
    y int
}
type Interval struct {
    left *Point
    right *Point
}
type Status struct {
    optimum *Interval
    graphMin *Point
}


func (point Point) print() string {
	return "(" + strconv.Itoa(point.x) + ", " + strconv.Itoa(point.y) + ")"	
}

func (interval Interval ) print() string{
	return "[" +  interval.left.print() + "," + interval.right.print() + "]"
}

func (status Status) print() string {
	return "Status[ optimum=" + status.optimum.print() + ", min=" + status.graphMin.print() + "]"
}

func (this Point) equal(that Comparable) bool {
	return (this.x == that.(Point).x) && (this.y == that.(Point).y)
}

func (this Interval) equal(that Comparable) bool {
	return (*this.left).equal(*(that.(Interval).left)) && (*this.right).equal(*(that.(Interval).right))
}

func (this Status) equal(that Comparable) bool {
	return (*this.graphMin).equal(*(that.(Status).graphMin)) && (*this.optimum).equal(*(that.(Status).optimum))
}

func profit(interval *Interval) int { 
    return (*interval.right).y - (*interval.left).y
}

func min(a *Point, b *Point) *Point { 
    if a.y <= b.y { 
    	return a 
    } else { 
    	return b
    }
}

func mostProfitable(left *Interval, right *Interval) *Interval {
    if profit(left) > profit(right) {
        return left
    } else {
        return right
    }
}

func currMax(status *Status, 
             currPoint *Point) *Status {
    if status == nil { 
        return &Status { &Interval{currPoint, currPoint}, currPoint }                         
   } else {    
        return  &Status { mostProfitable(&Interval{status.graphMin, currPoint}, status.optimum ),		
          			min(currPoint, status.graphMin) }
                
   }
}

func max(timeSortedPoints []Point) *Interval {
	var status *Status 
	for i := 0; i < len(timeSortedPoints); i++ {
		status = currMax(status, &timeSortedPoints[i])	
	}
	
	if(status == nil) { 
		return nil
	}
	
	return status.optimum
}