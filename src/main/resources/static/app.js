let stompClient = null
let scatterChart

let startTime = 0
const threTime = 30 * 1000
let packetCount = 0

$(function () {
    drawChart()
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).on("click", connect())
    $( "#disconnect" ).on("click", disconnect())
})

function setConnected(connected) {
    $("#connect").prop("disabled", connected)
    $("#disconnect").prop("disabled", !connected)
}

function connect() {
    const socket = new SockJS('/gs-guide-websocket')
    stompClient = Stomp.over(socket)
    stompClient.connect({}, function (frame) {
        setConnected(true)
        console.log('Connected: ' + frame)
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

function drawChart() {
    scatterChart = acgraph.create("container")
}

function addData(packets) {
    packetCount += packets.length
    if(startTime == 0) startTime = Date.now()
    if(Date.now() - startTime > threTime) {
        console.log({packetCount})
        disconnect()
    }

    scatterChart.suspend()
    packets.forEach(packet => {
        if(packet != null) {
            scatterChart.circle(packet.x * 3.5, packet.y * 3.5, 1.5).fill("black")
        }
    })
    scatterChart.resume()
}
