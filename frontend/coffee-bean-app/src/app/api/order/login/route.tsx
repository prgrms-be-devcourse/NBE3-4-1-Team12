import { NextResponse } from "next/server";

export async function GET(req: Request) {
  // 요청에서 쿼리 파라미터(email) 가져오기
  const { searchParams } = new URL(req.url);
  const email = searchParams.get("email");

  if (!email) {
    return NextResponse.json({ error: "이메일이 필요하다냥!" }, { status: 400 });
  }

  // 외부 API 요청 보내기
  const url = new URL("http://localhost:8080/api/order/login");
  url.searchParams.append("email", email);

  const response = await fetch(url.toString(), {
    method: "GET",
    headers: { "Content-Type": "application/json" },
  });

  // 응답 JSON 반환
  const data = await response.json();
  /*
  console.log("----------------------------------------");
  console.log("data:\n", JSON.stringify(data, null, 2));
  console.log("----------------------------------------");
  */
  return NextResponse.json(data);
}