


// Fetch swimlane data from the REST API
//fetch('http://localhost:8080/swimlaneData')
fetch('http://localhost:8080/wholeDay')
  .then(response => {
    console.info('Fetching data from REST service...');  // Info log
    if (!response.ok) {
      throw new Error('Network response was not ok ' + response.statusText);
    }
    return response.json();
  })
  .then(data => {
    console.log('Data fetched successfully:', data);  // Log the received data

    // Parse and format data
    data.forEach(task => {
      task.start = new Date(task.start);
      task.end = new Date(task.end);
    });

    console.info('Data processed and converted to Date objects.');  // Info log
    drawSwimlaneChart(data);  // Call the function to draw the swimlane chart
  })
  .catch(error => {
    console.error('Error fetching swimlane data:', error);  // Error log for any issues
  });

// Drawing function with additional logging
function drawSwimlaneChart(swimlaneData) {
  console.log('Drawing swimlane chart with data:', swimlaneData);  // Log the data being used

  const margin = { top: 20, right: 30, bottom: 30, left: 100 };
  const width = 800 - margin.left - margin.right;
  const height = 500 - margin.top - margin.bottom;

  console.debug('Chart dimensions:', { width, height });  // Debug-level logging for dimensions

  const lanes = [...new Set(swimlaneData.map(d => d.lane))];
  console.log('Swimlane categories:', lanes);  // Log the extracted swimlane categories

  // Create scales
  const xScale = d3.scaleTime()
    .domain([d3.min(swimlaneData, d => d.start), d3.max(swimlaneData, d => d.end)])
    .range([0, width]);

  const yScale = d3.scaleBand()
    .domain(lanes)
    .range([height,0]) // reversed range
    .padding(0.1);

  console.debug('Scales created:', { xScale, yScale });  // Debug the scales

  // Create SVG container
  const svg = d3.select("#swimlaneChart")
    .append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
    .append("g")
    .attr("transform", `translate(${margin.left},${margin.top})`);

  console.log('SVG element created');  // Log the creation of the SVG element

  // Add X-axis (Time)
  const xAxis = d3.axisBottom(xScale)
    .ticks(d3.timeMinute.every(60))
    .tickFormat(d3.timeFormat("%H:%M"));

  svg.append("g")
    .attr("class", "x-axis")
    .attr("transform", `translate(0,${height})`)
    .call(xAxis);

  console.log('X-axis added to the chart');  // Log after the X-axis is added

  // Add Y-axis (Lanes)
  svg.append("g")
    .attr("class", "y-axis")
    .call(d3.axisLeft(yScale));

  console.log('Y-axis added to the chart');  // Log after the Y-axis is added

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
    .attr("fill", d => d.color)
    .on("click", function(event, d) {
      console.log('Task clicked:', d);  // Log the task that was clicked
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
                  End: ${d.end.getHours()}:${d.end.getMinutes()}<br>
                  Elapsed Secs: ${d.elapsedSecs}<br>
                  Color: ${d.color}<br>
                  `);
    });


  console.info('Swimlane chart drawing completed.');  // Info log once the chart is drawn
}
      // Add a listener on the body to hide the tooltip when clicking outside of a task
      d3.select("body").on("click", function() {
        const tooltip = d3.select("#taskInfo");
        tooltip.style("display", "none");
      });

