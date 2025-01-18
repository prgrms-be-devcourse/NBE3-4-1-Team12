import { GetServerSideProps } from "next";
import { paging } from "./api"; // fetchOrders는 api.ts에서 가져옴
import { useSearchParams } from "next/navigation";

export default async function ServerSidePage() {
    const searchParams = useSearchParams();
    
        const page: number = parseInt(searchParams.get("page") || "0", 10);
        const pageSize: number = parseInt(searchParams.get("pageSize") || "10", 10);
        const email: string = searchParams.get("email") || "";

    return (
        <div>
            
        </div>
    );
}
