"use client"
import { useSearchParams } from "next/navigation";


export default function Page() {

    const searchParams = useSearchParams();

    const page: number = parseInt(searchParams.get("page") || "0", 10);
    const pageSize: number = parseInt(searchParams.get("pageSize") || "10", 10);
    const email: string = searchParams.get("email") || "";

    return(
        <div>
        paging 
        </div>
    );
}