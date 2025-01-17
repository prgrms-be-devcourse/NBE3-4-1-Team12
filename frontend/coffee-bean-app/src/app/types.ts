// 회원 정보 (CustomerDto)
export interface CustomerDto {
  email: string;  // 이메일
  address: string; // 주소
  postcode: string; // 우편번호
}

// 상품 정보 (PostDetailOrderDto)
export interface PostDetailOrderDto {
  id: number;  // 상품 ID
  
  quantity: number; // 구매 수량
}

// 주문 요청 DTO (PostOrderRequestDto)
export interface PostOrderRequestDto {
  customer: CustomerDto;  // 회원 정보
  products: PostDetailOrderDto[];  // 상품 리스트
}