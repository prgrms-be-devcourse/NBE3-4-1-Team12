import { PostOrderRequestDto } from "./types";

export async function createOrder(order: PostOrderRequestDto) {

  try {
    const response = await fetch("http://localhost:8080/api/order", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(order),
    });

    if (!response.ok) {
      throw new Error(`HTTP 오류! 상태 코드: ${response.status}`);
    }

    const responseData = await response.json();

    if (response.status === 201) {
      alert("당일 오후 2시 이후의 주문은 다음날 배송을 시작합니다.");
    } else {
      alert(responseData.msg || "오류가 발생했습니다.");
    }
    
    console.log(responseData);
    return responseData;
  } catch (error) {
    
    alert(
      `주문을 처리하는 중 오류가 발생했습니다.`
    );
    }
  }

  export async function login(email: string) {
    const url = new URL("http://localhost:8080/api/order/login");
    url.searchParams.append("email", email);
    const response = await fetch(url.toString(), {
        method: "GET",
        headers: { "Content-Type": "application/json" },
        
      });

    return response.json();
}