$(document).ready(function() {
    var form={
        name:"111",
        targetMoney:30,
        giftMoney:2,
        price:25
    };

    //getAllStrategies();
    renderStrategies([form]);

    /*function getAllStrategies() {
        getRequest(
            '/vipStrategy/get',
            function (res) {
                var strategies=res.content;
                renderStrategies(strategies);
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    }*/

    function renderStrategies(strategies) {
        $('.content-vipStrategy').empty();
        var strategiesDomStr="";

        strategies.forEach(function (strategy) {
            strategiesDomStr+=
                "<div class='strategy-container' >"+
                "    <div class='strategy-card card'>" +
                "       <div class='stratrgy-line'>" +
                "           <span class='title'>"+strategy.name+"</span>" +
                "       </div>" +
                "       <div class='stratrgy-line'>" +
                "           <span>目标金额："+strategy.targetMoney+"</span>" +
                "       </div>" +
                "       <div class='stratrgy-line'>" +
                "           <span>赠送金额："+strategy.giftMoney+"</span>" +
                "       </div>" +
                "    </div>" +
                "    <div class='strategy-price primary-bg'>" +
                "        <h1 class='price-show'>￥</h1>" +
                "        <div class='strategy-operations'>"+
                "            <button type='button' data-strategy='"+JSON.stringify(strategy)+"' id='strategy-edit-"+strategy.id+"'  class='strategy-edit btn btn-default' data-backdrop='static' data-toggle='modal' data-target='#strategyEditModal'><span>修改</span></button>" +
                "            <button type='button' data-strategy='"+JSON.stringify(strategy)+"' id='strategy-delete-"+strategy.id+"' class='strategy-delete btn btn-default' ><span>删除</span></button>" +
                "        </div>"+
                "    </div>" +
                "</div>";
        });
        $('.content-vipStrategy').append(strategiesDomStr);
    }
    //发布会员卡
    $('#strategy-form-btn').click(function () {
        var form={
            name:$('#strategy-name-input').val(),
            price:$('#strategy-price-input').val(),
            targetMoney:$('#target-money-input').val(),
            giftMoney:$('#gift-money-input').val()
        };

        postRequest(
            '/vipStrategy/publish',
            form,
            function (res) {
                if (res.success) {
                    getAllStrategies();
                    $('#strategyModal').modal('hide');
                } else {
                    alert(res.message);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    });

    //修改会员卡
    $(document).on('click','.strategy-edit',function (e) {
        var strategy = JSON.parse(e.target.dataset.strategy);
        $("#strategy-edit-name-input").val(strategy.name);
        $("#strategy-edit-price-input").val(strategy.price);
        $("#target-edit-money-input").val(strategy.targetMoney);
        $("#gift-edit-money-input").val(strategy.giftMoney);
        $('#strategyEditModal').modal('show');
        $('#strategyEditModal')[0].dataset.strategyId = strategy.id;

    });

    $('#strategy-edit-form-btn').click(function () {
        var form={
            id:Number($('#strategyEditModal')[0].dataset.strategyId),
            name:$('#strategy-edit-name-input').val(),
            price:$('#strategy-edit-price-input').val(),
            targetMoney:$('#target-edit-money-input').val(),
            giftMoney:$('#gift-edit-money-input').val()
        };
        postRequest(
            '/vipStrategy/update',
            form,
            function (res) {
                if (res.success) {
                    getAllStrategies();
                    $('#strategyEditModal').modal('hide');
                } else {
                    alert(res.message);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    });

    //删除会员卡
    $(document).on('click','.strategy-delete',function (e) {
        var r=confirm("确认要删除该会员策略吗");
        if (r){
            deleteRequest(
                '/vipStrategy/delete',
                Number(JSON.parse(e.target.dataset.strategy).id),
                function (res) {
                    if (res.success) {
                        getAllStrategies();
                    } else {
                        alert(res.message);
                    }
                },
                function (error) {
                    alert(JSON.stringify(error));
                }
            );
        }
    })

});