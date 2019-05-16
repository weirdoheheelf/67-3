$(document).ready(function () {
    getMovieList();

    function getMovieList() {
        getRequest(
            '/ticket/get/' + sessionStorage.getItem('id'),
            function (res) {
                renderTicketList(res.content);
            },
            function (error) {
                alert(error);
            });
    }

    // TODO:填空
    function renderTicketList(list) {
        var ticketDomStr = '';
        list.forEach(function (ticket) {
            /*ticketDomStr +=
                "<tbody>"+
                "<tr>"+
                "<td>"+
                "<div id='ticket-movie-name'>"+ticket.schedule.movieName+"</div>"+
                "</td>"+
                "<td>"+
                "<div id='ticket-hall-name'>"+ticket.schedule.hallName+"</div>"+
                "</td>"+
                "<td id='ticket-position'>" + ticket.rowIndex + "排" + ticket.columnIndex+ "座</div>"+
                "</td>"+
                "<td>"+
                "<div id='ticket-start-time'>"+ticket.schedule.startTime.toString().substring(11,19)+"</div>"+
                "</td>"+
                "<td id='ticket-end-time'>"+ticket.schedule.endTime.toLocaleString().substring(11,19)+"</td>"+
                "<td>"+"<b id='ticket-state'></b>"+ticket.state+"</td>"+
                "</tr>"+
                "</tbody>"
        });*/
            ticketDomStr += "<tr>" +
                "<td>" + ticket.schedule.movieName + "</td>" +
                "<td>" + ticket.schedule.hallName + "</td>" +
                "<td>" + (ticket.rowIndex+1) + "排" + (ticket.columnIndex+1) + "座" + "</td>" +
                "<td>" + ticket.schedule.startTime.toString().substring(11, 19) + "</td>" +
                "<td>" + ticket.schedule.endTime.toLocaleString().substring(11, 19) + "</td>" +
                "<td>" + ticket.state + "</td>" +
                "</tr>"
        });
        $('.table tbody').append(ticketDomStr);
    }
});