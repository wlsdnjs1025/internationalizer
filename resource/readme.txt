1. 파일 추가 
 - jworks/common/js/msg.js 추가
 - WEB-INF/src/jworks/core/MessageBundle.java 추가
 - WebService/Common/command/Message_Command.jns 추가

2. msg.js 라이브러리 import 코드 추가
 - incHTMLHeader.jns 2곳
 	-> biz.js 보다 상위에 추가해야 함

3. 메서드 추가
 - ScriptUtil.java initMessageScript(String lang)
 - biz.js 	
	
4. DB
 - jovt_member 테이블에 lang 컬럼 추가
 - def_lang 함수 추가 

5. locale 초기화 코드 추가
 - incDefaultHeader.jns
 - Lobby.jns 
 - Admin.jns
 - OT_Edit.jns
 
 6. 언어 변경 버튼 추가
 
 ------------------------------------------------------------------
 
 * 기타 수작업 필요
 1. OT_Command.jns '최대 1분' 포함된 부분
 	ReAuth_UI.js '운영자에게 결재를 요청합니다.' 포함된 부분
 
 	 홑따옴표대신 쌍따옴표를 HTML 태그 안에서 써서 문제가 됨
 	-> 홑따옴표로 바꿔서 Internationalization 실행 or 메시지 프로퍼티 직접 수정
 	