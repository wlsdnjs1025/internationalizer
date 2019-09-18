package jness.internationalization.model;

import java.util.HashMap;
import java.util.Map;

public enum ConstMsg {
	DEFAULT_MSG_ERROR_COMMON("알 수 없는 오류가 발생했습니다."),
	DEFAULT_MSG_ERROR_FILE("파일이 존재하지 않습니다."),
	DEFAULT_MSG_ERROR_FOLDER("폴더가 존재하지 않습니다."),
	DEFAULT_MSG_ERROR_CONN("서버에 접속할 수 없습니다."),
	DEFAULT_MSG_ERROR_OPEN("연결된 서버가 있습니다."),
	DEFAULT_MSG_ERROR_CLOSE("연결해제 오류가 발생했습니다."),
	DEFAULT_MSG_ERROR_TRAN("트랜잭션 오류가 발생했습니다."),
	DEFAULT_MSG_ERROR_QUERY("쿼리 실행이 실패했습니다."),
	DEFAULT_MSG_ERROR_BIND("일부 변수가 바인드되지 않았습니다."),
	DEFAULT_MSG_ERROR_RECORD("존재하지 않는 레코드 범위입니다."),
	DEFAULT_MSG_ERROR_COLUMN("은 존재하지 않는 컬럼명입니다."),
	DEFAULT_PRODUCT_NAME("PC-OFF 시스템"),
	DEFAULT_TREE_NAME("조직도"),
	DEFAULT_BASIC_WORK_NM("기본 근무표"),
	DEFAULT_TITLE_EMP("사번"),
	DEFAULT_TITLE_DPT("부서"),
	DEFAULT_TITLE_POS("직위"),
	DEFAULT_TITLE_TIT("직책"),
	DEFAULT_TITLE_JIK("직급"),
	DEFAULT_MSG_FAIL_BASIC_WORK("기본 근무표는 삭제할 수 없습니다."),
	DEFAULT_MSG_FAIL_CNT_WORK("사용 중인 근무표는 삭제할 수 없습니다."),
	DEFAULT_MSG_FAIL_NOT_SETTING("프로그램 설정값이 존재하지 않습니다."),
	DEFAULT_MSG_FAIL_BASIC_SETTING("\\n프로그램 설정값 확인 후 적용해주세요."),
	DEFAULT_MSG_FAIL_USE_EMP("이미 사용 중인 " + DEFAULT_TITLE_EMP + "입니다."),
	DEFAULT_MSG_FAIL_USE_ACCOUNT("이미 사용 중인 계정명입니다."),
	DEFAULT_MSG_FAIL_USE_DAY("이미 사용 중인 휴일입니다."),
	DEFAULT_MSG_FAIL_USE_GRP("이미 사용 중인 그룹명입니다."),
	DEFAULT_MSG_FAIL_USE_WORK("이미 사용 중인 근무표명입니다."),
	DEFAULT_MSG_FAIL_USE_DOMAIN("이미 등록되어 있는 제외사이트입니다."),
	DEFAULT_MSG_INFO_USE_EMP("기간 내에 이미 등록되어 있는 사원목록"),
	DEFAULT_MSG_INFO_USE_GRP("기간 내에 이미 등록되어 있는 그룹목록"),
	DEFAULT_MSG_INFO_USE_DPT("기간 내에 이미 등록되어 있는 " + DEFAULT_TITLE_DPT + "목록"),
	DEFAULT_MSG_FAIL_USE_NOTICE("동일한 사용용도/대상자/대상 요일/팝업시간으로\\n등록된 알림창 메시지가 있습니다."),
	DEFAULT_MSG_FAIL_USE_BLOCK("동일한 사용용도로\\n등록된 차단창 메시지가 있습니다."),
	DEFAULT_MSG_FAIL_DEL_MODE_0("공통 메시지는 삭제할 수 없습니다."),
	DEFAULT_MSG_FAIL_NOT_MODE_1("평일 차단전 알림창이 존재하지 않습니다.\\n메시지 설정에서 등록 후 사용해주세요."),
	DEFAULT_MSG_FAIL_NOT_MODE_2("특정일 차단전 알림창이 존재하지 않습니다.\\n메시지 설정에서 등록 후 사용해주세요."),
	DEFAULT_MSG_FAIL_NOT_MODE_3("차단요일 차단전 알림창이 존재하지 않습니다.\\n메시지 설정에서 등록 후 사용해주세요."),
	DEFAULT_MSG_FAIL_NOT_MODE_4("휴일 차단전 알림창이 존재하지 않습니다.\\n메시지 설정에서 등록 후 사용해주세요."),
	DEFAULT_MSG_FAIL_NOT_MODE_5("스마트 워킹데이 차단전 알림창이 존재하지 않습니다.\\n메시지 설정에서 등록 후 사용해주세요."),
	DEFAULT_MSG_FAIL_OT_APPROVER("해당 " + DEFAULT_TITLE_DPT + "에 결재자가 존재하지 않습니다.\\n운영자에게 문의해주세요."),
	DEFAULT_MSG_FAIL_OT_EXCLUSION("비대상자는 PC 사용 신청이 제한되어 있습니다."),
	DEFAULT_MSG_FAIL_OT_EXCEPTION("차단 예외자와 예외기간에는 PC 사용 신청이 제한되어 있습니다."),
	DEFAULT_MSG_FAIL_OT_VACATION("휴가일에는 PC 사용 신청이 제한되어 있습니다."),
	DEFAULT_MSG_FAIL_OT_TYPE_1("사전 PC 사용 신청이 제한되어 있습니다."),
	DEFAULT_MSG_FAIL_OT_TYPE_2("긴급 PC 사용 신청이 제한되어 있습니다."),
	DEFAULT_MSG_FAIL_OT_ALERT_0("신청일자의 사용한도를 초과하여 신청했습니다."),
	DEFAULT_MSG_FAIL_OT_ALERT_1("신청일시에 결재중인 신청건이 있습니다.\\n기존 신청건을 취소 후 신청해주세요."),
	DEFAULT_MSG_FAIL_OT_ALERT_2("신청일시에 승인완료된 신청건이 있습니다."),
	DEFAULT_MSG_FAIL_OT_ALERT_3("결재완료된 신청건은 취소할 수 없습니다."),
	DEFAULT_MSG_FAIL_OT_ALERT_4("취소처리된 신청건은 취소할 수 없습니다."),
	DEFAULT_MSG_WARN_PARAM("비정상적으로 접속하셨습니다."),
	DEFAULT_MSG_WARN_AUTH("사용 권한이 없습니다."),
	DEFAULT_MSG_WARN_EMP("사원정보가 존재하지 않습니다."),
	DEFAULT_MSG_WARN_SESSION("세션이 만료되었습니다."),
	DEFAULT_MSG_WARN_DECRYPT("암호화 오류가 발생했습니다.\\n시스템 관리자에게 문의해주세요."),
	DEFAULT_MSG_INFO_NODATA("검색된 데이터가 없습니다."),
	DEFAULT_MSG_INFO_NOADD("추가된 데이터가 없습니다."),
	DEFAULT_MSG_WARN_CHECKED("처리할 대상를 체크해주세요."),
	DEFAULT_MSG_WARN_SELECTED("처리할 상태를 선택해주세요."),
	DEFAULT_MSG_CONFIRM_SAVE("저장 하시겠습니까?"),
	DEFAULT_MSG_CONFIRM_DEL("삭제 하시겠습니까?"),
	DEFAULT_MSG_CONFIRM_SEND("발송 하시겠습니까?"),
	DEFAULT_MSG_CONFIRM_UPDATE("업데이트 하시겠습니까?"),
	DEFAULT_MSG_CONFIRM_CHANGE("변경 하시겠습니까?"),
	DEFAULT_MSG_CONFIRM_CANCEL("취소 하시겠습니까?"),
	DEFAULT_MSG_CONFIRM_ACTION("실행 하시겠습니까?"),
	DEFAULT_MSG_CONFIRM_EXCEL("현재 데이터를 엑셀로 저장하시겠습니까?"),
	DEFAULT_MSG_PROC_SAVE("성공적으로 저장되었습니다."),
	DEFAULT_MSG_PROC_DEL("성공적으로 삭제되었습니다."),
	DEFAULT_MSG_PROC_SEND("성공적으로 발송되었습니다."),
	DEFAULT_MSG_PROC_UPDATE("성공적으로 업데이트되었습니다."),
	DEFAULT_MSG_PROC_CHANGE("성공적으로 변경되었습니다."),
	DEFAULT_MSG_PROC_CANCEL("성공적으로 취소되었습니다."),
	DEFAULT_MSG_PROC_SUCCESS("정상적으로 처리되었습니다."),
	DEFAULT_MSG_PROC_FAIL("비정상적으로 종료되었습니다."),
	DEFAULT_MSG_LOGIN_FAIL("로그인 정보가 일치하지 않습니다."),
	DEFAULT_MSG_LOGOUT_CONFIRM("로그아웃 하시겠습니까?");
	
	private final String message;
	
	private ConstMsg(String message) {
		this.message = message;
	}
	
	public static Map<String, String> getPropertyMap() {
		Map<String, String> constMap = new HashMap<String, String>();
		int index = 0;
		
		for (ConstMsg msg : values()) {
			constMap.put(getKey(index), msg.message);
			index++;
		}
		
		return constMap;
	}
	
	private static String getKey(int index) {
		String key = "message_C";
		
		for (int i = 0; i < 3; i++) {
    		String indexStr = String.valueOf(index);
    		if (indexStr.length() - 1 < i) {
    			key += "0";
    		}
    	}
		
		return key + index;
	}
	
	public static String get(String line) {
		for (ConstMsg constMsg : values()) {
			if (line.contains(constMsg.name())) {
				return constMsg.name();
			}
		}
		
		return "";
	}
}
