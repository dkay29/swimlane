const margin = { top: 20, right: 30, bottom: 30, left: 100 };
const width = 800 - margin.left - margin.right;
const height = 500 - margin.top - margin.bottom;

const swimlaneData = [
  { name: "Task A", lane: "Team 1", start: new Date(2024, 9, 1, 10, 0), end: new Date(2024, 9, 1, 10, 30) },
  { name: "Task B", lane: "Team 1", start: new Date(2024, 9, 1, 10, 45), end: new Date(2024, 9, 1, 11, 15) },
  { name: "Task C", lane: "Team 2", start: new Date(2024, 9, 1, 10, 10), end: new Date(2024, 9, 1, 11, 0) },
  { name: "Task D", lane: "Team 2", start: new Date(2024, 9, 1, 11, 5), end: new Date(2024, 9, 1, 11, 45) },
  { name: "Task E", lane: "Team 3", start: new Date(2024, 9, 1, 10, 20), end: new Date(2024, 9, 1, 11, 10) }
];

const lanes = [...new Set(swimlaneData.map(d => d.lane))];

// Create scales
const xScale = d3.scaleTime()
  .domain([d3.min(swimlaneData, d => d.start), d3.max(swimlaneData, d => d.end)])
  .range([0, width]);

const yScale = d3.scaleBand()
  .domain(lanes)
  .range([0, height])
  .padding(0.1);

// Create SVG container
const svg = d3.select("#swimlaneChart")
  .append("svg")
  .attr("width", width + margin.left + margin.right)
  .attr("height", height + margin.top + margin.bottom)
  .append("g")
  .attr("transform", `translate(${margin.left},${margin.top})`);

// Add X-axis (Time)
const xAxis = d3.axisBottom(xScale)
  .ticks(d3.timeMinute.every(15))
  .tickFormat(d3.timeFormat("%H:%M"));

svg.append("g")
  .attr("class", "x-axis")
  .attr("transform", `translate(0,${height})`)
  .call(xAxis);

// Add Y-axis (Lanes)
svg.append("g")
  .attr("class", "y-axis")
  .call(d3.axisLeft(yScale));

// Draw the bars for each task
svg.selectAll(".task")
  .data(swimlaneData)
  .enter()
  .append("rect")
  .attr("class", "task")
  .attr("x", d => xScale(d.start))
  .attr("y", d => yScale(d.lane))
  .attr("width", d => xScale(d.end) - xScale(d.start))
  .attr("height", yScale.bandwidth())
  .attr("fill", "steelblue")
  .on("click", function(event, d) {
    // Prevent click event from bubbling up
    event.stopPropagation();

    // Get the tooltip div
    const tooltip = d3.select("#taskInfo");

    // Position the tooltip based on click event location
    tooltip.style("left", (event.pageX + 10) + "px")
      .style("top", (event.pageY - 10) + "px")
      .style("display", "block")
      .html(`<strong>${d.name}</strong><br/>
            Lane: ${d.lane}<br/>
            Start: ${d.start.getHours()}:${d.start.getMinutes()}<br/>
            End: ${d.end.getHours()}:${d.end.getMinutes()}`);
  });

// Add event listener to hide tooltip when clicking outside of a task
d3.select("body").on("click", function() {
  d3.select("#taskInfo").style("display", "none");
});
