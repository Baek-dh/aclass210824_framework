function sendMessage(){
    const message = $("#inputChatting").val();

    if(message.trim().length == 0){
        alert("내용을 입력해주세요.");
    }else{
        const obj={};
        obj.memberNo = memberNo;
        obj.memberId = memberId;
        obj.memberName = memberName;
        obj.message = message;
        obj.chatRoomNo = chatRoomNo;

        // console.log(obj);

        // 만들어진 JS 객체를 JSON으로 변환하여 웹소켓 객체 handleTextMessage() 로 전달

        // JSON.stringify : JS 객체 -> JSON 문자열
        // JSON.parse : JSON 문자열 ->  JS 객체
        chattingSock.send(JSON.stringify(obj))

        $("#inputChatting").val(""); // 전달된 메세지 지우기
    }
}


// 웹소켓 서버에서 전달된 메세지가 있을 경우 수행되는 이벤트
chattingSock.onmessage = function(e){

    // e.data : 전달 받은 메세지
    console.log(JSON.parse(e.data));

    // 메소드를 통해 전달받은 객체값을 JSON객체로 변환해서 obj 변수에 저장.
   const obj = JSON.parse(e.data);


   const li = $("<li>");

   const p = $("<p class='chat'>");
   const span = $("<span class='chatDate'>");
   span.html(obj.createDate);

   const chat = obj.message.replace(/\\n/g, "<br>");
   p.html(chat);


   if (obj.memberNo == memberNo) {
      li.addClass("myChat");
      li.append(span);
      li.append(p);
   } else {
      li.html("<b>" + obj.memberName + "</b><br>");
      li.append(p);
      li.append(span);
   }


   $(".display-chatting").append(li);

   // 채팅 입력 시 스크롤을 가장 아래로 내리기
   $(".display-chatting").scrollTop($(".display-chatting")[0].scrollHeight);
}


// 보내기 버튼 클릭 시 채팅 전달
$("#send").on("click", sendMessage);
