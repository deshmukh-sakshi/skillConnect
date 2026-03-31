import type { Wallet } from "@/types";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Wallet as WalletIcon, TrendingUp, Lock } from "lucide-react";
import { AddMoneyDialog } from "./add-money-dialog";

interface WalletSummarysProps {
  wallet: Wallet;
  refetchDetails: () => void;
}

const formatCurrency = (amount: number) =>
  new Intl.NumberFormat("en-IN", {
    style: "currency",
    currency: "INR",
    maximumFractionDigits: 2,
  }).format(amount);

const WalletSummary = ({ wallet, refetchDetails }: WalletSummarysProps) => {
  const totalBalance = wallet.availableBalance + wallet.frozenBalance;

  return (
    <Card className="bg-gradient-to-br from-white to-[#FAF8F5] dark:from-zinc-900 dark:to-zinc-800 shadow-lg border border-[#E5E0D8]">
      <CardHeader className="pb-4">
        <div className="flex justify-between items-start">
          <div className="space-y-1">
            <div className="flex items-center gap-2">
              <WalletIcon className="h-5 w-5 text-[#FF6B47]" />
              <CardTitle className="text-xl">Wallet Summary</CardTitle>
            </div>
            <p className="text-sm text-muted-foreground">
              Client Wallet Overview
            </p>
          </div>
          <AddMoneyDialog
            userId={wallet.userId}
            refetchDetails={refetchDetails}
          />
        </div>
      </CardHeader>

      <CardContent className="space-y-6">
        <div className="p-4 rounded-lg bg-gradient-to-r from-[#FF6B47]/12 to-[#2EC4B6]/12 dark:from-[#FF6B47]/10 dark:to-[#2EC4B6]/10 border border-[#E5E0D8] dark:border-zinc-700">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-[#1A1A2E] dark:text-zinc-200">
                Total Balance
              </p>
              <p className="text-2xl font-bold text-[#1A1A2E] dark:text-zinc-100">
                {formatCurrency(totalBalance)}
              </p>
            </div>
            <TrendingUp className="h-8 w-8 text-[#FF6B47]" />
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <Card className="border-[#2EC4B6]/35 dark:border-[#2EC4B6]/40 bg-[#2EC4B6]/10 dark:bg-[#2EC4B6]/10">
            <CardContent className="p-4">
              <div className="flex items-center justify-between">
                <div className="space-y-1">
                  <p className="text-sm font-medium text-[#1A1A2E] dark:text-zinc-200">
                    Available Balance
                  </p>
                  <p className="text-xl font-bold text-[#1A1A2E] dark:text-zinc-100">
                    {formatCurrency(wallet.availableBalance)}
                  </p>
                </div>
                <Badge
                  variant="secondary"
                  className="bg-[#2EC4B6]/25 text-[#1A1A2E] dark:bg-[#2EC4B6]/30 dark:text-zinc-100"
                >
                  Available
                </Badge>
              </div>
            </CardContent>
          </Card>

          <Card className="border-[#FFB900]/40 dark:border-[#FFB900]/35 bg-[#FFB900]/12 dark:bg-[#FFB900]/10">
            <CardContent className="p-4">
              <div className="flex items-center justify-between">
                <div className="space-y-1">
                  <p className="text-sm font-medium text-[#1A1A2E] dark:text-zinc-200">
                    Frozen Balance
                  </p>
                  <p className="text-xl font-bold text-[#1A1A2E] dark:text-zinc-100">
                    {formatCurrency(wallet.frozenBalance)}
                  </p>
                </div>
                <Lock className="h-5 w-5 text-[#FFB900]" />
              </div>
            </CardContent>
          </Card>
        </div>
      </CardContent>
    </Card>
  );
};

export default WalletSummary;
