// StockGO project main.go
package main


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

func profit(interval *Interval) int { 
    return interval.right.y - interval.left.y
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