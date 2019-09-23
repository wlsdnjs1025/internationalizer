1. 파일 추가 
 - jworks/common/js/msg.js 추가
 - WEB-INF/src/jworks/core/MessageBundle.java 추가
 - WebService/Common/command/Message_Command.jns 추가

2. msg.js 라이브러리 import 코드 추가
 - incHTMLHeader.jns 2곳
 	-> biz.js 보다 상위에 추가해야 함

3. 메서드 추가
 - ScriptUtil.java initMessageScript(String lang)
	
4. DB
 - jovt_member 테이블에 lang 컬럼 추가
 - def_lang 함수 추가 

5. locale 초기화 코드 추가
 - incDefaultHeader.jns
 - Lobby.jns 
 - Gate.jns
 - Admin.jns
 - OT_Edit.jns
 
 6. 언어 변경 버튼 추가
 
 ------------------------------------------------------------------
 
 * 한계 (수작업 필요)
 
 1. HTML 태그 안에서 홑따옴표대신 쌍따옴표를 써서 문제가 되는 부분 (4군데)
 	-> 메시지 프로퍼티 직접 수정 
 
 2. DB에서 가져오는 한글은 getWord 함수로 변환해줘야 함
 
 3. 버튼에 박혀있는 한글들...
 
 4. 번역했을 때 레이아웃 깨짐, 이미지 잘림
 
 5. Const.java 한글을 읽어와서 enum 만들도록..
 
 6. 프로퍼티 파일이 존재하면 , 하위에 추가  (여러번 돌릴 수 있게)
 
 7. 단어 사전