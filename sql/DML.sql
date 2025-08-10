-- =====================================================================================================================
-- tb_account_role: `role` default values
-- =====================================================================================================================
insert into tb_account_role(id, name) values('R0000', 'ROLE_ADMIN');
insert into tb_account_role(id, name) values('R0100', 'ROLE_MANAGER');
insert into tb_account_role(id, name) values('R0200', 'ROLE_USER');


-- =====================================================================================================================
-- tb_common_group_code: `group_code` default values
-- =====================================================================================================================
INSERT INTO tb_common_group_code (group_code, group_name, description, is_active, created_at, updated_at)
VALUES
('SOCIAL_PROVIDER_TYPE', '소셜 로그인 제공자 유형', '외부 OAuth 제공자', TRUE, now(), now()),
('ACCOUNT_STATUS_TYPE', '계정 상태 유형', '계정 상태 유형 코드', TRUE, now(), now()),
('ACCOUNT_BAN_TYPE', '계정 벤 유형', '계정 벤 유형 코드', TRUE, now(), now()),
('BOARD_TYPE', '게시판 유형', '게시판 유형 코드', TRUE, now(), now()),
('ATTACHMENT_TYPE', '첨부파일 유형', '첨부파일 유형 코드', TRUE, now(), now()),
('ATTACHMENT_MIME_TYPE', '허용 첨부파일 MIME 유형', '정규 MIME 유형 코드', TRUE, now(), now()),
('ATTACHMENT_MIME_ALIAS_TYPE', 'MIME 별칭', '레거시/변형 → 정규 MIME 매핑 코드', TRUE, now(), now()),
('ATTACHMENT_USAGE_TYPE', '첨부파일 용도 유형', '첨부파일 용도 유형 코드', TRUE, now(), now()),
('REPORT_TYPE', '신고 유형', '스팸/사기 유형 코드', TRUE, now(), now()),
('REPORT_REVIEW_TYPE', '신고 판정 유형', '신고 판정 유형 코드', TRUE, now(), now()),
('REPORT_STATUS_TYPE', '신고 상태 유형', '신고 상태 유형 코드', TRUE, now(), now())
ON CONFLICT (group_code) DO NOTHING;

-- =====================================================================================================================
-- tb_common_code: `code` default values
-- =====================================================================================================================
-- SOCIAL_PROVIDER_TYPE
INSERT INTO tb_common_code (group_code, code, code_name, description, sort_order, is_active, created_at, updated_at)
VALUES
('SOCIAL_PROVIDER_TYPE', 'NAVER', '네이버', '', 10, TRUE, now(), now()),
('SOCIAL_PROVIDER_TYPE', 'GOOGLE', '구글', '', 20, TRUE, now(), now())
ON CONFLICT (group_code, code) DO NOTHING;

-- ACCOUNT_STATUS_TYPE
INSERT INTO tb_common_code (group_code, code, code_name, description, sort_order, is_active, created_at, updated_at)
VALUES
('ACCOUNT_STATUS_TYPE', 'ACTIVE', '활성화', '', 10, TRUE, now(), now()),
('ACCOUNT_STATUS_TYPE', 'INACTIVE', '비활성화', '', 20, TRUE, now(), now()),
('ACCOUNT_STATUS_TYPE', 'LOCKED', '잠금', '', 30, TRUE, now(), now()),
('ACCOUNT_STATUS_TYPE', 'BANNED', '정지', '', 40, TRUE, now(), now()),
('ACCOUNT_STATUS_TYPE', 'WITHDRAWN', '탈퇴', '', 50, TRUE, now(), now())
ON CONFLICT (group_code, code) DO NOTHING;

-- ACCOUNT_BAN_TYPE
INSERT INTO tb_common_code (group_code, code, code_name, description, sort_order, is_active, created_at, updated_at)
VALUES
('ACCOUNT_BAN_TYPE', 'CAUTION', '경고', '', 10, TRUE, now(), now()),
('ACCOUNT_BAN_TYPE', 'TEMPORARY', '기간 정지', '', 20, TRUE, now(), now()),
('ACCOUNT_BAN_TYPE', 'PERMANENT', '영구 정지', '', 30, TRUE, now(), now())
ON CONFLICT (group_code, code) DO NOTHING;

-- BOARD_TYPE
INSERT INTO tb_common_code (group_code, code, code_name, description, sort_order, is_active, created_at, updated_at)
VALUES
('BOARD_TYPE', 'NOTICE', '공지사항', '', 10, TRUE, now(), now())
ON CONFLICT (group_code, code) DO NOTHING;

-- ATTACHMENT_USAGE_TYPE
INSERT INTO tb_common_code (group_code, code, code_name, description, sort_order, is_active, created_at, updated_at)
VALUES
('ATTACHMENT_USAGE_TYPE', 'FILE', '첨부파일', '', 10, TRUE, now(), now()),
('ATTACHMENT_USAGE_TYPE', 'INLINE', '본문 삽입', '', 20, TRUE, now(), now()),
('ATTACHMENT_USAGE_TYPE', 'THUMBNAIL', '썸네일',   '', 30, TRUE, now(), now())
ON CONFLICT (group_code, code) DO NOTHING;

-- ATTACHMENT_TYPE
INSERT INTO tb_common_code (group_code, code, code_name, description, sort_order, is_active, created_at, updated_at)
VALUES
('ATTACHMENT_TYPE', 'IMAGE', '이미지', '', 10, TRUE, now(), now()),
('ATTACHMENT_TYPE', 'VIDEO', '영상',   '',20, TRUE, now(), now()),
('ATTACHMENT_TYPE', 'AUDIO', '오디오', '', 30, TRUE, now(), now()),
('ATTACHMENT_TYPE', 'DOC', '문서', '', 40, TRUE, now(), now()),
('ATTACHMENT_TYPE', 'ARCHIVE', '압축', '', 50, TRUE, now(), now()),
('ATTACHMENT_TYPE', 'OTHER', '기타',   '', 90, TRUE, now(), now())
ON CONFLICT (group_code, code) DO NOTHING;

-- ATTACHMENT_MIME_TYPE
INSERT INTO tb_common_code (group_code, code, code_name, description, sort_order, is_active, created_at, updated_at)
VALUES
('ATTACHMENT_MIME_TYPE','image/jpeg','JPEG','이미지(JPEG)',10,true,now(),now()),
('ATTACHMENT_MIME_TYPE','image/png','PNG','이미지(PNG)',20,true,now(),now()),
('ATTACHMENT_MIME_TYPE','image/webp','WEBP','이미지(WEBP)',30,true,now(),now()),
('ATTACHMENT_MIME_TYPE','video/mp4','MP4','동영상(MP4)',110,true,now(),now()),
('ATTACHMENT_MIME_TYPE','video/webm','WEBM','동영상(WEBM)',120,true,now(),now()),
('ATTACHMENT_MIME_TYPE','audio/mpeg','MP3','오디오(MP3)',210,true,now(),now()),
('ATTACHMENT_MIME_TYPE','audio/mp4','M4A','오디오(M4A)',230,true,now(),now()),
('ATTACHMENT_MIME_TYPE','application/pdf','PDF','문서(PDF)',310,true,now(),now()),
('ATTACHMENT_MIME_TYPE','application/vnd.openxmlformats-officedocument.wordprocessingml.document','DOCX','문서(DOCX)',320,true,now(),now()),
('ATTACHMENT_MIME_TYPE','application/vnd.openxmlformats-officedocument.spreadsheetml.sheet','XLSX','문서(XLSX)',330,true,now(),now()),
('ATTACHMENT_MIME_TYPE','application/vnd.openxmlformats-officedocument.presentationml.presentation','PPTX','문서(PPTX)',340,true,now(),now()),
('ATTACHMENT_MIME_TYPE','application/vnd.hancom.hwp','HWP','문서(HWP)',350,true,now(),now()),
('ATTACHMENT_MIME_TYPE','application/vnd.hancom.hwpx','HWPX','문서(HWPX)',360,true,now(),now()),
('ATTACHMENT_MIME_TYPE','text/plain','TXT','문서(TXT)',370,true,now(),now()),
('ATTACHMENT_MIME_TYPE','application/zip','ZIP','압축(ZIP)',410,true,now(),now())
ON CONFLICT (group_code, code) DO NOTHING;

-- ATTACHMENT_MIME_ALIAS_TYPE
INSERT INTO tb_common_code (group_code, code, code_name, description, sort_order, is_active, created_at, updated_at)
VALUES
('ATTACHMENT_MIME_ALIAS_TYPE','image/jpg','image/jpeg','JPEG 별칭(legacy IE/라이브러리)',10,true,now(),now()),
('ATTACHMENT_MIME_ALIAS_TYPE','image/pjpeg','image/jpeg','Progressive JPEG 별칭',11,true,now(),now()),
('ATTACHMENT_MIME_ALIAS_TYPE','image/x-png','image/png','PNG 별칭(구형 브라우저)',20,true,now(),now()),
('ATTACHMENT_MIME_ALIAS_TYPE','audio/x-wav','audio/wav','WAV 별칭',30,true,now(),now()),
('ATTACHMENT_MIME_ALIAS_TYPE','audio/mp3','audio/mpeg','MP3 별칭',31,true,now(),now()),
('ATTACHMENT_MIME_ALIAS_TYPE','application/x-zip-compressed','application/zip','ZIP 별칭(Windows)',40,true,now(),now()),
('ATTACHMENT_MIME_ALIAS_TYPE','application/x-pdf','application/pdf','PDF 별칭',50,true,now(),now()),
('ATTACHMENT_MIME_ALIAS_TYPE','application/x-hwp','application/vnd.hancom.hwp','HWP 별칭',60,true,now(),now())
ON CONFLICT (group_code, code) DO NOTHING;

-- REPORT_TYPE
INSERT INTO tb_common_code (group_code, code, code_name, description, sort_order, is_active, created_at, updated_at)
VALUES
('REPORT_TYPE', 'LOAN', '대출', '불법 대출 권유, 고금리 유도', 10, TRUE, now(), now()),
('REPORT_TYPE', 'TELEMARKETING', '텔레마케팅', '상품/서비스를 광고하려는 전화 마케팅', 20, TRUE, now(), now()),
('REPORT_TYPE', 'GAMBLING', '불법 도박', '스포츠토토, 슬롯머신 등 불법 도박 사이트 유도', 30, TRUE, now(), now()),
('REPORT_TYPE', 'ADULT', '성인/유흥', '유흥업소, 성인 채팅, 성매매 알선 등', 40, TRUE, now(), now()),
('REPORT_TYPE', 'INSURANCE', '보험 광고', '보험 상담, 가입 권유 등 과도한 영업', 50, TRUE, now(), now()),
('REPORT_TYPE', 'SURVEY', '여론조사', '설문조사 명목으로 개인 정보 수집', 60, TRUE, now(), now()),
('REPORT_TYPE', 'INVESTMENT', '투자 사기', '고수익 보장형 투자 권유 (코인, 펀드 등)', 70, TRUE, now(), now()),
('REPORT_TYPE', 'IMPERSONATION', '기관 사칭', '경찰, 금융기관, 정부 등으로 사칭해 협박 및 개인정보 요구', 80, TRUE, now(), now()),
('REPORT_TYPE', 'ETC', '기타', '기타 사유', 90, TRUE, now(), now())
ON CONFLICT (group_code, code) DO NOTHING;

-- REPORT_REVIEW_TYPE
INSERT INTO tb_common_code (group_code, code, code_name, description, sort_order, is_active, created_at, updated_at)
VALUES
('REPORT_REVIEW_TYPE', 'PENDING', '대기',   '', 10, TRUE, now(), now()),
('REPORT_REVIEW_TYPE', 'AUTO',    '자동 판단',   '', 20, TRUE, now(), now()),
('REPORT_REVIEW_TYPE', 'MANUAL',  '수동 판단',   '', 30, TRUE, now(), now())
ON CONFLICT (group_code, code) DO NOTHING;

-- REPORT_STATUS_TYPE
INSERT INTO tb_common_code (group_code, code, code_name, description, sort_order, is_active, created_at, updated_at)
VALUES
('REPORT_STATUS_TYPE', 'PENDING', '미판정', '', 10, TRUE, now(), now()),
('REPORT_STATUS_TYPE', 'SPAM', '스팸', '', 20, TRUE, now(), now()),
('REPORT_STATUS_TYPE', 'RELEASED', '해제', '', 30, TRUE, now(), now())
ON CONFLICT (group_code, code) DO NOTHING;