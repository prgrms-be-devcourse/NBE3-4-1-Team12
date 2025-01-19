import { NextResponse } from "next/server";

export async function GET(req: Request) {
    const { searchParams } = new URL(req.url);
  
    
    const page = searchParams.get("page") || "0";
    const pageSize=searchParams.get("pageSize") || "10";
    const email= searchParams.get("email") || "";

   const url = new URL("http://localhost:8080/api/order/history");
   url.searchParams.append("page", page);
   url.searchParams.append("pageSize", pageSize);
   url.searchParams.append("email", email);
   
     const response = await fetch(url.toString(), {
       method: "GET",
       headers: { "Content-Type": "application/json" },
     });
   
     // 응답 JSON 반환
     const data = await response.json();
     console.log("api/order/history - router.ts")
     /*
     console.log("----------------------------------------");
     console.log("data:\n", JSON.stringify(data, null, 2));
     console.log("----------------------------------------");
     */
     return NextResponse.json(data);
}
