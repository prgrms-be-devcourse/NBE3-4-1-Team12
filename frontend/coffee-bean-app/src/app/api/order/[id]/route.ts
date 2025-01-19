import { NextResponse } from "next/server";

export async function DELETE(req: Request, { params }: { params: { id: string } })  {
    const { id } = params; // URL의 id 가져오기
    
    console.log("-----------------------------------------------");
    console.log(`Deleting order ${id}`);
    console.log("-----------------------------------------------");
    
       
    const response = await fetch(`http://localhost:8080/api/order/${id}`, {
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