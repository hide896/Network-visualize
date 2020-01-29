let stompClient = null
let scatterChart

let startTime = 0
const threTime = 60 * 1000
let packetCount = 0

let graphData = anychart.data.set([{x: 123, y: 123}, {x: 2, y: 245}, {x: 254, y: 2}])

$(function () {
    drawChart()
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).on("click", connect())
    $( "#disconnect" ).on("click", disconnect())
    $( "#send" ).on("click", sendName())
})

function setConnected(connected) {
    $("#connect").prop("disabled", connected)
    $("#disconnect").prop("disabled", !connected)
    if (connected) {
        $("#conversation").show()
    }
    else {
        $("#conversation").hide()
    }
    $("#greetings").html("")
}

function connect() {
    const socket = new SockJS('/gs-guide-websocket')
    stompClient = Stomp.over(socket)
    stompClient.connect({}, function (frame) {
        setConnected(true)
        console.log('Connected: ' + frame)
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content)
        })
        stompClient.subscribe('/topic/packets', function (packets) {
            addData(JSON.parse(packets.body))
        })
    })
    stompClient.debug = null
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect()
    }
    setConnected(false)
    console.log("Disconnected")
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}))
}

function drawChart() {
    scatterChart = anychart.scatter()
    var series = scatterChart.marker(graphData)
    series.size(1)
    scatterChart.xGrid(true)
    scatterChart.yGrid(true)
    scatterChart.xMinorGrid(true)
    scatterChart.yMinorGrid(true)
    scatterChart.container("container")
    scatterChart.draw()
}

function addData(packets) {
    for(let i in packets) {
        graphData.append({x: packets[i].x, y: packets[i].y})
    }
    // if(startTime == 0) startTime = Date.now()
    // packetCount += packets.length
    // if(Date.now() - startTime > threTime) {
    //     console.log({packetCount})
    //     disconnect()
    // }
    // Array.prototype.push.apply(scatterChart.data.datasets[0].data, packets)
    // scatterChart.update()
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}