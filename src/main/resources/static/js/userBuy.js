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
        $('.ticket-on-list').empty();
        var ticketDomStr =
            "<table class='table table-striped table-bordered'>"+
            "<thead>"+ "<tr>"+ "<th>电影名称</th>" +"<th>影厅名</th>"+
            "<th>座位</th>"+
            "<th>放映时间</th>"+
            "<th>预计结束时间</th>"+
            "<th>状态</th>"+
            "</tr>"+ "</thead>";
        list.forEach(function (ticket) {
            ticketDomStr +=
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
        });
        ticketDomStr=ticketDomStr+"</table>"
        $('.ticket-on-list').append(ticketDomStr);
    }

});