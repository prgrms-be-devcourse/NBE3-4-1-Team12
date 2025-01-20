import { NextResponse } from "next/server";

export async function DELETE(req: Request, { params }: { params: { orderId: string } })  {
    const { orderId } = params; // URL의 id 가져오기
    
    console.log("-----------------------------------------------");
    console.log(`Deleting order ${orderId}`);
    console.log("-----------------------------------------------");
    
       
    const response = await fetch(`http://localhost:8080/api/order/${orderId}`, {
        method: "DELETE",
        headers: { "Content-Type": "application/json" },
    });

    const data = await response.json();
      
    console.log("Delete api/order/id - router.ts")          
    console.log("----------------------------------------");
    console.log("data:\n", JSON.stringify(data, null, 2));
    console.log("----------------------------------------");
                 
    return NextResponse.json(data);
    
}

export async function PUT(req: Request, context: { params: { orderId: string } }) {
    const { params } = context; // 🔹 params를 먼저 가져옴
    const orderId = params.orderId; // 🔹 이후에 id를 사용

    console.log("-----------------------------------------------");
    console.log(`Put order ${orderId}`);
    console.log("-----------------------------------------------");

    try {
        const requestBody = await req.json(); // 🔹 요청의 body 데이터 가져오기
        console.log("Received request body:", requestBody);

        const response = await fetch(`http://localhost:8080/api/order/${orderId}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(requestBody), // 🔹 받은 body 데이터를 그대로 백엔드로 전달
        });

        const resData = await response.json(); // 응답 데이터 받기
        console.log("Backend response:", resData);

        return new Response(JSON.stringify(resData), {
            status: response.status,
            headers: { "Content-Type": "application/json" },
        });
    } catch (error) {
        console.error("Error processing PUT request:", error);
        return new Response(JSON.stringify({ error: "Failed to process request" }), {
            status: 500,
            headers: { "Content-Type": "application/json" },
        });
    }
}